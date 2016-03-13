package com.infinityworks.webapp.paf.client;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.common.LambdaLogger;
import com.infinityworks.webapp.config.CanvassConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.*;

@Component
public class Http {
    private final LambdaLogger log = LambdaLogger.getLogger(Http.class);
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final RestTemplate restTemplate;
    private final PafErrorHandler pafErrorHandler;
    public static final String EMPTY_BODY = "";

    @Autowired
    public Http(RestTemplate restTemplate,
                CanvassConfig config,
                PafErrorHandler pafErrorHandler) {
        this.restTemplate = restTemplate;
        this.pafErrorHandler = pafErrorHandler;
        this.httpHeaders.set("X-Authorization", config.getPafApiToken());
        this.httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        this.httpHeaders.setAccept(singletonList(MediaType.APPLICATION_JSON));
    }

    public <T> Try<T> get(String url, Class<T> responseType) {
        return request(url, responseType, GET);
    }

    public <T> Try<T> delete(String url, Class<T> response) {
        return request(url, response, DELETE);
    }

    public <T, U> Try<U> put(String url, T content, Class<U> responseType) {
        return request(url, content, responseType, PUT);
    }

    public <T, U> Try<U> post(String url, T content, Class<U> responseType) {
        return request(url, content, responseType, POST);
    }

    private <T, U> Try<U> request(String url, T content, Class<U> responseType, HttpMethod method) {
        HttpEntity<T> httpEntity = new HttpEntity<>(content, httpHeaders);
        return request(url, responseType, method, httpEntity);
    }

    private <U> Try<U> request(String url, Class<U> responseType, HttpMethod method) {
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        return request(url, responseType, method, httpEntity);
    }

    private <U> Try<U> request(String url, Class<U> responseType, HttpMethod method, HttpEntity httpEntity) {
        UUID correlationKey = UUID.randomUUID();
        long startTime = System.currentTimeMillis();
        log.debug(() -> String.format("Paf Request[%s] %s %s", correlationKey, method, url));

        try {
            ResponseEntity<U> responseEntity = restTemplate.exchange(url, method, httpEntity, responseType);
            return Try.success(responseEntity.getBody());
        } catch (Exception e) {
            String msg = String.format("%s " + url, method);
            return pafErrorHandler.handleError(msg, e);
        } finally {
            log.debug(() -> {
                long endTime = System.currentTimeMillis();
                return String.format("Paf Response[%s] %s %s. Time Taken(ms)=%s", correlationKey, method, url, endTime - startTime);
            });
        }
    }
}
