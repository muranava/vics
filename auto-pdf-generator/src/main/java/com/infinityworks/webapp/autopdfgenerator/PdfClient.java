package com.infinityworks.webapp.autopdfgenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import com.infinityworks.webapp.autopdfgenerator.dto.GeneratePdfRequest;
import com.infinityworks.webapp.autopdfgenerator.dto.StreetsResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

class PdfClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client = HttpClients.custom().build();
    private final String createGotvCardUrl;
    private final String getStreetsUrl;
    private final String apiToken;

    PdfClient(String pdfServerUrl, String pafApiUrl, String apiToken) {
        this.createGotvCardUrl = pdfServerUrl + "/canvass/gotv";
        this.getStreetsUrl = pafApiUrl + "/wards/%s/streets";
        this.apiToken = apiToken;
    }

    StreetsResponse streetsByWard(String wardCode) throws IOException {
        String url = String.format(getStreetsUrl, wardCode);
        HttpUriRequest request = RequestBuilder.get()
                .setUri(url)
                .setHeader(HttpHeaders.ACCEPT, MediaType.JSON_UTF_8.toString())
                .setHeader("X-Authorization", apiToken)
                .build();
        return client.execute(request, response -> {
            InputStream is = new BufferedInputStream(response.getEntity().getContent());
            return Json.objectMapper.readValue(is, StreetsResponse.class);
        });
    }

    Optional<byte[]> createGotvCard(GeneratePdfRequest pdfRequest) throws IOException {
        StringEntity body = new StringEntity(serializeContent(pdfRequest));
        body.setContentType("application/json");

        HttpPost postRequest = new HttpPost(createGotvCardUrl);
        postRequest.setEntity(body);
        postRequest.setHeader(HttpHeaders.ACCEPT, "application/pdf");

        HttpResponse response = client.execute(postRequest);
        if (response.getStatusLine().getStatusCode() == 404) {
            return Optional.empty();
        } else {
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            entity.writeTo(baos);
            return Optional.of(baos.toByteArray());
        }
    }

    private String serializeContent(GeneratePdfRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            String msg = String.format("Failed to serialize content, request=%s. Aborting...", request);
            throw new IllegalStateException(msg);
        }
    }

}
