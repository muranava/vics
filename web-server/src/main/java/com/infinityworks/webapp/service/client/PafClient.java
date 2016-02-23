package com.infinityworks.webapp.service.client;

import com.infinityworks.common.lang.StringExtras;
import com.infinityworks.common.lang.Try;
import com.infinityworks.commondto.Property;
import com.infinityworks.commondto.Voter;
import com.infinityworks.commondto.VotersByStreet;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.error.PafApiFailure;
import com.infinityworks.webapp.error.ServerFailure;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import com.infinityworks.webapp.rest.dto.SearchElectors;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.rest.dto.TownStreets;
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

@Component
public class PafClient {
    private static final Logger log = LoggerFactory.getLogger(PafClient.class);
    private static final String DOWNSTREAM_ERROR_MESSAGE = "PAF api failure. Contact your system administrator";
    private static final String STREETS_BY_WARD_ERROR_MESSAGE = "PAF request failed when getting streets by wardCode=%s. %s";
    private static final String RECORD_VOTE_ERROR_MESSAGE = "PAF request failed when recording vote ern=%s. Paf responded with %s";
    private static final String ADD_CONTACT_ERROR_MESSAGE = "PAF request failed when adding contact record ern=%s. Paf responded with %s";
    private static final String ELECTORS_BY_STREET_ERROR_MESSAGE = "PAF request failed when getting electors by street=%s. %s";

    private final String API_TOKEN;
    private final String STREETS_BY_WARD_ENDPOINT;
    private final String ELECTORS_BY_STREET_ENDPOINT;
    private final String VOTED_ENDPOINT;
    private final String CONTACT_ENDPOINT;

    private final RestTemplate restTemplate;
    private final StreetConverter streetConverter;
    private final String pafApiBaseUrl;

    @Autowired
    public PafClient(RestTemplate restTemplate, CanvassConfig canvassConfig, StreetConverter streetConverter) {
        this.restTemplate = restTemplate;
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
     * @param wardCode the ward code to retrieve streets by, e.g. E09000125
     * @return
     */
    public Try<List<Street>> findStreetsByWardCode(String wardCode) {
        String url = String.format(STREETS_BY_WARD_ENDPOINT, wardCode);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", API_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Street[]> pafResponse;

        try {
            pafResponse = restTemplate.exchange(url, GET, httpEntity, Street[].class);
        } catch (HttpClientErrorException e) {
            String msg = String.format(STREETS_BY_WARD_ERROR_MESSAGE, wardCode, "");
            log.error(msg, e);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }

        List<Street> records = asList(pafResponse.getBody());
        return Try.success(records);
    }

    public Try<List<VotersByStreet>> findElectorsByStreet(TownStreets street, String wardCode) {
        String url = String.format(ELECTORS_BY_STREET_ENDPOINT, wardCode);

        ResponseEntity<VotersByStreet[]> pafResponse;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", API_TOKEN);
        List<StreetRequest> streets = street.getStreets()
                .stream()
                .map(streetConverter)
                .collect(toList());

        HttpEntity<List<StreetRequest>> entity = new HttpEntity<>(streets, headers);

        try {
            pafResponse = restTemplate.exchange(url, HttpMethod.POST, entity, VotersByStreet[].class);
        } catch (HttpClientErrorException e) {
            String msg = String.format(ELECTORS_BY_STREET_ERROR_MESSAGE, street, " Paf responded with " + e.getStatusText());
            log.error(msg);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }

        List<VotersByStreet> records = asList(pafResponse.getBody());
        for (VotersByStreet vbs : records) {
            for (Property property : vbs.getProperties()) {
                // FIXME fake data for demo
                property.getVoters().add(new Voter("PD-AB123",   "R987BB", "07831441567", "1451", "", "", "Amy", "J", "Smith" ));
                property.getVoters().add(new Voter("PD-GB123-1", "RE9141", "07831441563", "1144", "1", "", "Michael", "", "Smith"));
                property.getVoters().add(new Voter("PD-GB123-2", "E98141", "07831441563", "2515", "2", "", "Tim", "N", "Boon"));
            }
        }
        return Try.success(records);
    }

    /**
     * Records that an elector has voted.  Not found responses from PAF are treated as
     * success in the context of the application (users will type ids at a fast rate and
     * erroneous inputs are expected) and we set a failure flag in the return entity.
     * @param recordVote
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
            return Try.success(new RecordVote(ern, recordVote.getPollingDistrict(), recordVote.getWardCode(), true));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                return Try.success(new RecordVote(ern, recordVote.getPollingDistrict(), recordVote.getWardCode(), false));
            } else {
                return Try.failure(new ServerFailure(String.format(RECORD_VOTE_ERROR_MESSAGE, ern, e.getStatusCode().getReasonPhrase())));
            }
        }
    }

    public Try<RecordContactRequest> recordContact(String ern, RecordContactRequest contactRecord) {
        String url = String.format(CONTACT_ENDPOINT, ern);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", API_TOKEN);
        HttpEntity<RecordContactRequest> entity = new HttpEntity<>(contactRecord, headers);

        try {
            ResponseEntity<RecordContactRequest> response = restTemplate.exchange(url, HttpMethod.PUT, entity, RecordContactRequest.class);
            log.debug("Recorded voter voted PUT {}", url);
            return Try.success(response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                String message = String.format("No voter with ern=%s. PAF returned %s", ern, e.getStatusCode().getReasonPhrase());
                log.debug(message);
                return Try.failure(new NotFoundFailure(message, ern));
            } else {
                return Try.failure(new ServerFailure(String.format(ADD_CONTACT_ERROR_MESSAGE, ern, e.getStatusCode().getReasonPhrase())));
            }
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
            return Try.failure(new PafApiFailure(String.format(ADD_CONTACT_ERROR_MESSAGE, searchElectors, e.getStatusCode().getReasonPhrase())));
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
