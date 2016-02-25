package com.infinityworks.webapp.testsupport.stub;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.io.Resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Wiremock server so the PAF api can be stubbed in tests
 */
public class PafServerStub {
    private static final int PAF_SERVER_PORT = 9002;
    private final WireMockServer pafMockServer = new WireMockServer(wireMockConfig().port(PAF_SERVER_PORT));

    private WireMock wireMock;

    private static final Map<String, String> files = new HashMap<>();
    static {
        files.put("E05001221", "json/paf-streets-earlsdon.json");
        files.put("E05001221,Coventry", "paf-voters-multiple-streets.json");
        files.put("voted,ADD-1313-1", "json/paf-record-voted.json");
        files.put("search,McCall,KT25BU", "json/paf-search-voter.json");
    }

    public void start() {
        pafMockServer.start();
        wireMock = new WireMock("localhost", pafMockServer.port());
    }

    public void stop() {
        pafMockServer.stop();
    }

    public void willReturnStreetsByWard(String wardCode) throws IOException {
        String file = requireNonNull(files.get(wardCode), "No json file for ward=" + wardCode);
        String jsonData = Resources.toString(getResource(file), UTF_8);

        String urlPath = String.format("/v1/wards/%s/streets", wardCode);
        wireMock.register(get(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(jsonData)));
    }

    public void willReturnVotersByWardByTownAndByStreet(String wardCode, String town) throws IOException {
        String file = requireNonNull(files.get(String.format("%s,%s", wardCode, town)), "No json file for town=" + town);
        String jsonData = Resources.toString(getResource(file), UTF_8);

        String urlPath = String.format("/v1/wards/%s/streets", wardCode);
        wireMock.register(post(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(jsonData)));
    }

    public void willRecordVoterVoted(String ern) throws IOException {
        String file = requireNonNull(files.get("voted," + ern), "No json file for voted request ern=" + ern);
        String jsonData = Resources.toString(getResource(file), UTF_8);

        String urlPath = "/v1/voter/" + ern;
        wireMock.register(put(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(jsonData)));
    }

    public void willSearchVoters(String lastName, String postCode) throws IOException {
        String fileName = String.format("search,%s,%s", lastName, postCode);
        String file = requireNonNull(files.get(fileName),
                String.format("No json file for voted request lastName=%s, postCode=%s", lastName, postCode));
        String jsonData = Resources.toString(getResource(file), UTF_8);

        String urlPath = "/v1/voter";
        wireMock.register(get(urlPathMatching(urlPath))
                .withQueryParam("lastName", equalTo("McCall"))
                .withQueryParam("postCode", equalTo("KT25BU"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(jsonData)));
    }
}
