package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {
    private final String buildingNumber;
    private final String mainStreet;
    private final String postTown;
    private final String dependentLocality;
    private final String dependentStreet;
    private final List<Voter> voters;

    @JsonCreator
    public Property(@JsonProperty("building_number") String buildingNumber,
                    @JsonProperty("main_street") String mainStreet,
                    @JsonProperty("post_town") String postTown,
                    @JsonProperty("dependent_locality") String dependentLocality,
                    @JsonProperty("dependent_street") String dependentStreet,
                    @JsonProperty("voter") List<Voter> voters) {
        this.buildingNumber = buildingNumber;
        this.mainStreet = mainStreet;
        this.postTown = postTown;
        this.dependentLocality = dependentLocality;
        this.dependentStreet = dependentStreet;
        this.voters = voters;
    }

    public String getPostTown() {
        return postTown;
    }

    public String getDependentLocality() {
        return dependentLocality;
    }

    public String getDependentStreet() {
        return dependentStreet;
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
