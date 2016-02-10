package com.infinityworks.webapp.service.client;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.error.PafApiFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@Component
public class PafClient {
    private static final Logger log = LoggerFactory.getLogger(PafClient.class);
    private static final String PAF_API_FAILURE = "PAF request failed when getting streets by wardCode=%s. %s";
    private final String STREETS_BY_WARD_ENDPOINT;

    private final RestTemplate restTemplate;
    private final HttpEntity httpEntity;

    @Autowired
    public PafClient(RestTemplate restTemplate, CanvassConfig canvassConfig) {
        this.restTemplate = restTemplate;
        STREETS_BY_WARD_ENDPOINT = canvassConfig.getPafApiBaseUrl() + "/wards/%s/streets";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        this.httpEntity = new HttpEntity(headers);
    }

    public Try<List<Street>> findStreetsByWardCode(String wardCode) {
        String url = String.format(STREETS_BY_WARD_ENDPOINT, wardCode);

        ResponseEntity<Street[]> pafResponse;
        try {
            pafResponse = restTemplate.exchange(url, GET, httpEntity, Street[].class);
        } catch (Exception e) {
            String msg = String.format(PAF_API_FAILURE, wardCode, "");
            log.error(msg, e);
            return Try.failure(new PafApiFailure(PAF_API_FAILURE));
        }

        if (pafResponse.getStatusCode() == OK) {
            List<Street> records = asList(pafResponse.getBody());
            return Try.success(records);
        } else {
            String msg = String.format(PAF_API_FAILURE, wardCode, " Paf responded with " + pafResponse.getHeaders().toString());
            log.error(msg);
            return Try.failure(new PafApiFailure(PAF_API_FAILURE));
        }
    }
}
