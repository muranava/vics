package com.infinityworks.webapp.testsupport.stub;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.io.Resources;
import org.springframework.http.MediaType;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * Wiremock server so the PAF api can be stubbed in tests
 */
public class PafApi {
    private static final int PAF_SERVER_PORT = 9006;
    private final WireMockServer pafMockServer =
            new WireMockServer(wireMockConfig().port(PAF_SERVER_PORT));

    private WireMock wireMock;
    private final String pafExampleResponse;

    public PafApi() {
        try {
            pafExampleResponse = Resources.toString(getResource("json/paf-example.json"), UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read example paf response");
        }
    }

    public void start() {
        pafMockServer.start();
        wireMock = new WireMock("localhost", pafMockServer.port());
    }

    public void stop() {
        pafMockServer.stop();
    }

    public void willReturnPafForWard(String wardCode) {
        wireMock.register(get(urlPathMatching("/paf"))
                .withQueryParam("ward", equalTo(wardCode))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(pafExampleResponse)));
    }
}
