package com.infinityworks.canvass.pafstub;

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

/**
 * Wiremock server so the PAF api can be stubbed in tests
 */
public class PafServerStub {
    private final WireMockServer pafMockServer;
    private final int port;

    private WireMock wireMock;

    public PafServerStub(int port) {
        this.port = port;
        pafMockServer = new WireMockServer(wireMockConfig().port(port));
    }

    private static final Map<String, String> files = new HashMap<>();

    static {
        files.put("E05001221", "json/paf-streets-earlsdon.json");
        files.put("E05001221,Coventry", "json/paf-voters-multiple-streets.json");
        files.put("voted,E05001221-ADD-1313-1", "json/paf-record-voted.json");
        files.put("search,McCall,KT25BU", "json/paf-search-voter.json");
        files.put("postContact,E05001221-PD-123-4", "json/paf-record-contact-response.json");
    }

    public void start() {
        pafMockServer.start();
        wireMock = new WireMock("localhost", pafMockServer.port());
        System.out.println("Running wiremock paf stub on port " + port);
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
                        .withHeader(CONTENT_TYPE, "application/json")
                        .withBody(jsonData)));
    }

    /**
     * Returns the same streets no matter what ward you request
     */
    public void willReturnStreets() throws IOException {
        String jsonData = Resources.toString(getResource("json/paf-streets-E05001233.json"), UTF_8);

        String urlPath = "/v1/wards/.*/streets";
        wireMock.register(get(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, "application/json")
                        .withBody(jsonData)));
    }

    public void willDeleteAContactRecordFor(String ern, String contactId) throws IOException {
        String urlPath = "/v1/voter/" + ern + "/contact/" + contactId;
        wireMock.register(delete(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                .withHeader(CONTENT_TYPE, "application/json")));
    }

    /**
     * Returns the same voters no matter what streets you post
     */
    public void willReturnVotersByStreets() throws IOException {
        String jsonData = Resources.toString(getResource("json/paf-voters-leeds.json"), UTF_8);

        String urlPath = "/v1/wards/.*/streets";
        wireMock.register(post(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, "application/json")
                        .withBody(jsonData)));
    }

    /**
     * Returns the same voters no matter what streets you post
     */
    public void willReturnPropertiesWithoutVoters() throws IOException {
        String jsonData = Resources.toString(getResource("json/paf-properties-no-voters.json"), UTF_8);

        String urlPath = "/v1/wards/.*/streets";
        wireMock.register(post(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, "application/json")
                        .withBody(jsonData)));
    }

    public void willReturnVotersByWardByTownAndByStreet(String wardCode, String town) throws IOException {
        String file = requireNonNull(files.get(String.format("%s,%s", wardCode, town)), "No json file for town=" + town);
        String jsonData = Resources.toString(getResource(file), UTF_8);

        String urlPath = String.format("/v1/wards/%s/streets", wardCode);
        wireMock.register(post(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, "application/json")
                        .withBody(jsonData)));
    }

    public void willRecordVoterVoted(String ern) throws IOException {
        String file = requireNonNull(files.get("voted," + ern), "No json file for voted request ern=" + ern);
        String jsonData = Resources.toString(getResource(file), UTF_8);

        String urlPath = "/v1/voter/" + ern + "/voted";
        wireMock.register(post(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(jsonData)));
    }

    public void willRecordVoterVoted() throws IOException {
        String file = requireNonNull(files.get("voted,E05001221-ADD-1313-1"), "No json file");
        String jsonData = Resources.toString(getResource(file), UTF_8);

        String urlPath = "/v1/voter/.*/voted";
        wireMock.register(post(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(jsonData)));
    }

    public void willCreateANewContactRecord(String ern) throws IOException {
        String fileName = String.format("postContact,%s", ern);
        String file = requireNonNull(files.get(fileName),
                String.format("No json file for POST contact request ern=%s", ern));
        String stubResponse = Resources.toString(getResource(file), UTF_8);

        String urlPath = String.format("/v1/voter/%s", ern);
        wireMock.register(post(urlPathMatching(urlPath))
                .willReturn(aResponse()
                        .withBody(stubResponse)
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, "application/json")));
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
                        .withHeader(CONTENT_TYPE, "application/json")
                        .withBody(jsonData)));
    }

    private static class Nil {}
}
