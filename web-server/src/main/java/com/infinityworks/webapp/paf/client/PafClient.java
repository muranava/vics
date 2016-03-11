package com.infinityworks.webapp.paf.client;

import com.infinityworks.common.lang.StringExtras;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.converter.PafToStreetConverter;
import com.infinityworks.webapp.paf.converter.StreetToPafConverter;
import com.infinityworks.webapp.paf.dto.*;
import com.infinityworks.webapp.rest.dto.SearchElectors;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static com.infinityworks.webapp.paf.client.Http.EMPTY_BODY;
import static java.util.stream.Collectors.toList;

@Component
public class PafClient {
    private final String STREETS_BY_WARD_ENDPOINT;
    private final String ELECTORS_BY_STREET_ENDPOINT;
    private final String VOTED_ENDPOINT;
    private final String CONTACT_ENDPOINT;

    private final Http http;
    private final StreetToPafConverter streetConverter;
    private final PafToStreetConverter pafToStreetConverter;
    private final String pafApiBaseUrl;

    @Autowired
    public PafClient(PafToStreetConverter pafStreetConverter,
                     Http http,
                     CanvassConfig canvassConfig,
                     StreetToPafConverter streetConverter) {
        pafToStreetConverter = pafStreetConverter;
        this.http = http;
        this.streetConverter = streetConverter;
        pafApiBaseUrl = canvassConfig.getPafApiBaseUrl();

        STREETS_BY_WARD_ENDPOINT = pafApiBaseUrl + "/wards/%s/streets";
        ELECTORS_BY_STREET_ENDPOINT = pafApiBaseUrl + "/wards/%s/streets";
        VOTED_ENDPOINT = pafApiBaseUrl + "/voter/%s/voted";
        CONTACT_ENDPOINT = pafApiBaseUrl + "/voter/%s/contact";
    }

    /**
     * Searches voters by attributes.
     *
     * @param searchElectors the search criteria
     * @return a collection of voters matching the criteria
     */
    public Try<List<Voter>> searchElectors(SearchElectors searchElectors) {
        String url = buildSearchUrl(searchElectors);
        return http
                .get(url, Voter[].class)
                .map(Arrays::asList);
    }

    /**
     * Records that an elector has voted.  Not found responses from PAF are treated as
     * success in the context of the application (users will type ids at a fast rate and
     * erroneous inputs are expected) and we set a failure flag in the return entity.
     *
     * @param ern the details of the voter to record
     */
    public Try<String> recordVoted(String ern) {
        String url = String.format(VOTED_ENDPOINT, ern);
        return http.post(url, EMPTY_BODY, String.class);
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

    /**
     * Records a voters intentions after a contact has been made
     *
     * @param ern           the electoral roll number of the voter that has voted
     * @param contactRecord the contact data, including voting intention, issues and flags
     * @return the contact record TODO not sure what PAF returns until API implemented
     */
    public Try<RecordContactRequest> recordContact(String ern, RecordContactRequest contactRecord) {
        String url = String.format(CONTACT_ENDPOINT, ern);
        return http.post(url, contactRecord, RecordContactRequest.class);
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
