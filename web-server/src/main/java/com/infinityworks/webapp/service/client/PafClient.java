package com.infinityworks.webapp.service.client;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.error.PafApiFailure;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.rest.dto.TownStreets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Component
public class PafClient {
    private static final Logger log = LoggerFactory.getLogger(PafClient.class);
    private static final String DOWNSTREAM_ERROR_MESSAGE = "PAF api failure. Contact your system administrator";
    private static final String STREETS_BY_WARD_ERROR_MESSAGE = "PAF request failed when getting streets by wardCode=%s. %s";
    private static final String RECORD_VOTE_ERROR_MESSAGE = "PAF request failed when recording vote ern=%s. %s";
    private static final String ELECTORS_BY_STREET_ERROR_MESSAGE = "PAF request failed when getting electors by street=%s. %s";
    private final String API_TOKEN;
    private final String STREETS_BY_WARD_ENDPOINT;
    private final String ELECTORS_BY_STREET_ENDPOINT;
    private final String VOTED_ENDPOINT;

    private final RestTemplate restTemplate;
    private final StreetConverter streetConverter;

    @Autowired
    public PafClient(RestTemplate restTemplate, CanvassConfig canvassConfig, StreetConverter streetConverter) {
        this.restTemplate = restTemplate;
        this.streetConverter = streetConverter;

        STREETS_BY_WARD_ENDPOINT = canvassConfig.getPafApiBaseUrl() + "/wards/%s/streets";
        ELECTORS_BY_STREET_ENDPOINT = canvassConfig.getPafApiBaseUrl() + "/wards/%s/streets";
        VOTED_ENDPOINT = canvassConfig.getPafApiBaseUrl() + "/voted/%s";
        API_TOKEN = canvassConfig.getPafApiToken();
    }

    public Try<List<Street>> findStreetsByWardCode(String wardCode) {
        String url = String.format(STREETS_BY_WARD_ENDPOINT, wardCode);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", API_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Street[]> pafResponse;

        try {
            pafResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Street[].class);
        } catch (HttpClientErrorException e) {
            String msg = String.format(STREETS_BY_WARD_ERROR_MESSAGE, wardCode, "");
            log.error(msg, e);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }

        if (pafResponse.getStatusCode() == HttpStatus.OK) {
            List<Street> records = asList(pafResponse.getBody());
            return Try.success(records);
        } else {
            String msg = String.format(STREETS_BY_WARD_ERROR_MESSAGE, wardCode, " Paf responded with " + pafResponse.getHeaders().toString());
            log.error(msg);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }
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
        } catch (RestClientException e) {
            String msg = String.format(ELECTORS_BY_STREET_ERROR_MESSAGE, street, "");
            log.error(msg, e);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }

        if (pafResponse.getStatusCode() == HttpStatus.OK) {
            List<VotersByStreet> records = asList(pafResponse.getBody());
            for (VotersByStreet vbs : records) {
                for (Property property : vbs.getProperties()) {
                    property.getVoters().add(new Voter("EAF", "07831441567", "R987BB", "1", "", "Amy", "Langley", "Smith"));
                    property.getVoters().add(new Voter("EAF", "07831441563", "RE9141", "1", "", "Michael", "Langley", "Smith"));
                    property.getVoters().add(new Voter("EAF", "07831441563", "E98141", "2", "", "Tim", "Boon", "S"));
                }
            }
            return Try.success(records);
        } else {
            String msg = String.format(ELECTORS_BY_STREET_ERROR_MESSAGE, street, " Paf responded with " + pafResponse.getHeaders().toString());
            log.error(msg);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }
    }

    /**
     * Records that an elector has voted.  Not found responses from PAF are treated as
     * success in the context of the application (users will type ids at a fast rate and
     * erroneous inputs are expected) and we set a failure flag in the return entity.
     */
    public Try<RecordVoteResponse> recordVoted(String ern) {
        String url = String.format(VOTED_ENDPOINT, ern);

        ResponseEntity<String> pafResponse;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", API_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            pafResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        } catch (RestClientException e) {
            String msg = String.format(RECORD_VOTE_ERROR_MESSAGE, ern, "");
            log.error(msg, e);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }

        if (pafResponse.getStatusCode() == HttpStatus.OK) {
            return Try.success(new RecordVoteResponse(ern, true));
        } else if (pafResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Try.success(new RecordVoteResponse(ern, false));
        } else {
            String msg = String.format(RECORD_VOTE_ERROR_MESSAGE, ern, " Paf responded with " + pafResponse.getHeaders().toString());
            log.error(msg);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }
    }
}
