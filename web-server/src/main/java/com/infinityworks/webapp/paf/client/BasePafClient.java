package com.infinityworks.webapp.paf.client;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.error.PafApiFailure;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;

public class BasePafClient {
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final RestTemplate restTemplate;

    public BasePafClient(RestTemplate restTemplate, CanvassConfig canvassConfig) {
        this.restTemplate = restTemplate;
        this.httpHeaders.set("X-Authorization", canvassConfig.getPafApiToken());
        this.httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        this.httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public <T> Try<ResponseEntity<T>> get(String url, Class<T> responseType) {
        try {
            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            ResponseEntity<T> pafResponse = restTemplate.exchange(url, GET, httpEntity, responseType);
            return Try.success(pafResponse);
        } catch (Exception e) {
            return Try.failure(new PafApiFailure("Failed"));
        }
    }
}
