package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {
    private final String buildingNumber;
    private final String mainStreet;
    private final List<Voter> voters;

    @JsonCreator
    public Property(@JsonProperty("building_number") String buildingNumber,
                    @JsonProperty("main_street") String mainStreet,
                    @JsonProperty("voter") List<Voter> voters) {
        this.buildingNumber = buildingNumber;
        this.mainStreet = mainStreet;
        this.voters = voters;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public String getMainStreet() {
        return mainStreet;
    }

    public List<Voter> getVoters() {
        return voters;
    }
}
