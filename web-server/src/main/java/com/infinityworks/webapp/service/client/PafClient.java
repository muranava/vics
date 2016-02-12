package com.infinityworks.webapp.service.client;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.error.PafApiFailure;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.rest.dto.TownStreets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class PafClient {
    private static final Logger log = LoggerFactory.getLogger(PafClient.class);
    private static final String DOWNSTREAM_ERROR_MESSAGE = "PAF api failure. Contact your system administrator";
    private static final String STREETS_BY_WARD_ERROR_MESSAGE = "PAF request failed when getting streets by wardCode=%s. %s";
    private static final String ELECTORS_BY_STREET_ERROR_MESSAGE = "PAF request failed when getting electors by street=%s. %s";
    private final String API_TOKEN;
    private final String STREETS_BY_WARD_ENDPOINT;
    private final String ELECTORS_BY_STREET_ENDPOINT;

    private final RestTemplate restTemplate;
    private final StreetConverter streetConverter;

    @Autowired
    public PafClient(RestTemplate restTemplate, CanvassConfig canvassConfig, StreetConverter streetConverter) {
        this.restTemplate = restTemplate;
        this.streetConverter = streetConverter;

        STREETS_BY_WARD_ENDPOINT = canvassConfig.getPafApiBaseUrl() + "/wards/%s/streets";
        ELECTORS_BY_STREET_ENDPOINT = canvassConfig.getPafApiBaseUrl() + "/wards/%s/streets";
        API_TOKEN = canvassConfig.getPafApiToken();
    }

    public Try<List<Street>> findStreetsByWardCode(String wardCode) {
        String url = String.format(STREETS_BY_WARD_ENDPOINT, wardCode);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", API_TOKEN);
        headers.setContentType(APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Street[]> pafResponse;

        try {
            pafResponse = restTemplate.exchange(url, GET, httpEntity, Street[].class);
        } catch (HttpClientErrorException e) {
            String msg = String.format(STREETS_BY_WARD_ERROR_MESSAGE, wardCode, "");
            log.error(msg, e);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }

        if (pafResponse.getStatusCode() == OK) {
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
        List<StreetRequest> streets = street.getStreets().stream().map(streetConverter::apply).collect(toList());
        HttpEntity<List<StreetRequest>> entity = new HttpEntity<>(streets, headers);

        try {
            ResponseEntity<String> s = restTemplate.exchange(url, POST, entity, String.class);
            pafResponse = restTemplate.exchange(url, POST, entity, VotersByStreet[].class);
        } catch (RestClientException e) {
            String msg = String.format(ELECTORS_BY_STREET_ERROR_MESSAGE, street, "");
            log.error(msg, e);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }

        if (pafResponse.getStatusCode() == OK) {
            List<VotersByStreet> records = asList(pafResponse.getBody());
            return Try.success(records);
        } else {
            String msg = String.format(ELECTORS_BY_STREET_ERROR_MESSAGE, street, " Paf responded with " + pafResponse.getHeaders().toString());
            log.error(msg);
            return Try.failure(new PafApiFailure(DOWNSTREAM_ERROR_MESSAGE));
        }
    }
}
