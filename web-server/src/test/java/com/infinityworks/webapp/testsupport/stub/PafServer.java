package com.infinityworks.webapp.testsupport.stub;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.io.Resources;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static java.util.Objects.requireNonNull;

/**
 * Wiremock server so the PAF api can be stubbed in tests
 */
public class PafServer {
    private static final int PAF_SERVER_PORT = 9002;
    private final WireMockServer pafMockServer = new WireMockServer(wireMockConfig().port(PAF_SERVER_PORT));

    private WireMock wireMock;

    private static final Map<String, String> wardFiles = new HashMap<>();
    static {
        wardFiles.put("E05001221", "json/paf-streets-earlsdon.json");
    }

    public void start() {
        pafMockServer.start();
        wireMock = new WireMock("localhost", pafMockServer.port());
    }

    public void stop() {
        pafMockServer.stop();
    }

    public void willReturnStreetsByWard(String wardCode) throws IOException {
        String file = requireNonNull(wardFiles.get(wardCode), "No json file for ward=" + wardCode);
        String jsonData = Resources.toString(getResource(file), UTF_8);

        String urlPath = String.format("/v1/wards/%s/streets", wardCode);
        wireMock.register(get(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonData)));
    }
}
