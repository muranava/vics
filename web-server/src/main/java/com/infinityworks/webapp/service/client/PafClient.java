package com.infinityworks.webapp.service.client;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.error.PafApiFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@Component
public class PafClient {
    private static final String PAF_API_FAILURE = "Failed to request voters by ward from PAF. PAF responded with";
    private static final String ELECTORS_BY_WARD_ENDPOINT = "/paf/ward/%s";

    private final RestTemplate restTemplate;
    private final CanvassConfig canvassConfig;
    private final HttpEntity httpEntity;

    @Autowired
    public PafClient(RestTemplate restTemplate, CanvassConfig canvassConfig) {
        this.restTemplate = restTemplate;
        this.canvassConfig = canvassConfig;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        this.httpEntity = new HttpEntity(headers);
    }

    /**
     * Finds the PAF records for the given ward
     *
     * @param wardCode the ward code to retrieve records for
     * @return the PAF records associated with the given ward
     */
    public Try<List<PafRecord>> findByWard(String wardCode) {
        String url = String.format(canvassConfig.getPafApiBaseUrl() + ELECTORS_BY_WARD_ENDPOINT, wardCode);
        ResponseEntity<PafRecord[]> pafResponse = restTemplate.exchange(url, GET, httpEntity, PafRecord[].class);
        if (pafResponse.getStatusCode() == OK) {
            List<PafRecord> records = asList(pafResponse.getBody());
            return Try.success(records);
        } else {
            return Try.failure(new PafApiFailure(PAF_API_FAILURE));
        }
    }
}
