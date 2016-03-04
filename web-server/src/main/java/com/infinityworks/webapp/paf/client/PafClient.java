package com.infinityworks.webapp.paf.client;

import com.infinityworks.common.lang.StringExtras;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.converter.PafToStreetConverter;
import com.infinityworks.webapp.error.PafApiFailure;
import com.infinityworks.webapp.error.ServerFailure;
import com.infinityworks.webapp.paf.converter.StreetToPafConverter;
import com.infinityworks.webapp.paf.dto.*;
import com.infinityworks.webapp.rest.dto.SearchElectors;
import com.infinityworks.webapp.rest.dto.Street;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.GET;

/**
 * TODO split this client up into separate functional classes.
 * TODO Rework exception handling
 */
@Component
public class PafClient {
    private static final Logger log = LoggerFactory.getLogger(PafClient.class);
    private static final String DOWNSTREAM_ERROR_MESSAGE = "PAF api failure. Contact your system administrator";
    private static final String RECORD_VOTE_ERROR_MESSAGE = "PAF request failed when recording vote ern=%s. Paf responded with %s";
    private static final String ADD_CONTACT_ERROR_MESSAGE = "PAF request failed when adding contact record ern=%s. Paf responded with %s";
    private static final String ELECTORS_BY_STREET_ERROR_MESSAGE = "PAF request failed when getting electors by streets. %s";

    private final String API_TOKEN;
    private final String STREETS_BY_WARD_ENDPOINT;
    private final String ELECTORS_BY_STREET_ENDPOINT;
    private final String VOTED_ENDPOINT;
    private final String CONTACT_ENDPOINT;

    private final RestTemplate restTemplate;
    private final Http http;
    private final StreetToPafConverter streetConverter;
    private final PafToStreetConverter pafToStreetConverter;
    private final String pafApiBaseUrl;

    @Autowired
    public PafClient(PafToStreetConverter pafStreetConverter,
                     RestTemplate restTemplate,
                     Http http,
                     CanvassConfig canvassConfig,
                     StreetToPafConverter streetConverter) {
        pafToStreetConverter = pafStreetConverter;
        this.restTemplate = restTemplate;
        this.http = http;
        this.streetConverter = streetConverter;
        pafApiBaseUrl = canvassConfig.getPafApiBaseUrl();

        STREETS_BY_WARD_ENDPOINT = pafApiBaseUrl + "/wards/%s/streets";
        ELECTORS_BY_STREET_ENDPOINT = pafApiBaseUrl + "/wards/%s/streets";
        VOTED_ENDPOINT = pafApiBaseUrl + "/voter/%s";
        CONTACT_ENDPOINT = pafApiBaseUrl + "/voter/%s/contact";
        API_TOKEN = canvassConfig.getPafApiToken();
    }

    /**
     * Finds all the streets in a given ward.
     *
     * @param wardCode the ward code to retrieve streets by, e.g. E09000125
     * @return a collection of streets
     */
    public Try<List<Street>> findStreetsByWardCode(String wardCode) {
        String url = String.format(STREETS_BY_WARD_ENDPOINT, wardCode);
        return http
                .get(url, StreetsResponse.class)
                .map(streets -> streets.response().stream()
                        .map(pafToStreetConverter)
                        .collect(toList()));
    }

    /**
     * Finds the voters in a given street.
     *
     * @param streets  the street where voters are contained
     * @param wardCode the ward code to restrict the result set by
     * @return A collection of properties grouped by street
     */
    public Try<PropertyResponse> findVotersByStreet(List<Street> streets, String wardCode) {
        String url = String.format(ELECTORS_BY_STREET_ENDPOINT, wardCode);

        List<PafStreet> pafStreets = streets
                .stream()
                .map(streetConverter)
                .collect(toList());

        return http.post(url, pafStreets, PropertyResponse.class);
    }

    public Try<RecordContactRequest> recordContact(String ern, RecordContactRequest contactRecord) {
        String url = String.format(CONTACT_ENDPOINT, ern);
        return http.post(url, contactRecord, RecordContactRequest.class);
    }

    /**
     * Records that an elector has voted.  Not found responses from PAF are treated as
     * success in the context of the application (users will type ids at a fast rate and
     * erroneous inputs are expected) and we set a failure flag in the return entity.
     *
     * @param recordVote the details of the voter to record
     */
    public Try<RecordVote> recordVoted(RecordVote recordVote) {
        String ern = recordVote.getErn();
        String url = String.format(VOTED_ENDPOINT, ern);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", API_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            log.debug("Recorded voter voted PUT {}", url);
            RecordVote vote = new RecordVote(recordVote.getWardCode(), recordVote.getWardName(), ern, true);
            return Try.success(vote);
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException) {
                if (((HttpClientErrorException) e).getStatusCode().value() == 404) {
                    RecordVote vote = new RecordVote(recordVote.getWardCode(), recordVote.getWardName(), ern, false);
                    return Try.success(vote);
                }
            }
            String message = String.format("Paf call to record vote failed. ern=%s. Paf responded with %s", ern, e.getMessage());
            log.error(message);
            return Try.failure(new ServerFailure(String.format(RECORD_VOTE_ERROR_MESSAGE, ern, e.getMessage())));
        }
    }

    public Try<List<Voter>> searchElectors(SearchElectors searchElectors) {
        String url = buildSearchUrl(searchElectors);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", API_TOKEN);
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Voter[]> response = restTemplate.exchange(url, GET, entity, Voter[].class);
            log.debug("Recorded voter voted PUT {}", url);
            return Try.success(asList(response.getBody()));
        } catch (HttpClientErrorException e) {
            return Try.failure(new PafApiFailure(String.format("Paf request failed when searching elector. " +
                    "search=%s. Paf responded with %s", searchElectors, e.getStatusCode().getReasonPhrase())));
        }
    }

    private String buildSearchUrl(SearchElectors searchElectors) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (!StringExtras.isNullOrEmpty(searchElectors.getFirstName())) {
            params.add("firstName", searchElectors.getFirstName());
        }
        if (!StringExtras.isNullOrEmpty(searchElectors.getLastName())) {
            params.add("lastName", searchElectors.getLastName());
        }
        if (!StringExtras.isNullOrEmpty(searchElectors.getAddress())) {
            params.add("address", searchElectors.getAddress());
        }
        if (!StringExtras.isNullOrEmpty(searchElectors.getPostCode())) {
            params.add("postCode", searchElectors.getPostCode());
        }
        return pafApiBaseUrl + UriComponentsBuilder.fromPath("/voter")
                .queryParams(params)
                .build()
                .toUriString();
    }
}
