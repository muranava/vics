package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.infinityworks.webapp.converter.GeneratesStreetLabel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Collections.emptyList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Property implements GeneratesStreetLabel {
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
                    @JsonProperty("voters") List<Voter> voters) {
        this.buildingNumber = buildingNumber;
        this.mainStreet = mainStreet;
        this.postTown = postTown;
        this.dependentLocality = dependentLocality;
        this.dependentStreet = dependentStreet;
        this.voters = firstNonNull(voters, new ArrayList<>());
    }

    @Override
    public String getPostTown() {
        return postTown;
    }

    @Override
    public String getDependentLocality() {
        return dependentLocality;
    }

    @Override
    public String getDependentStreet() {
        return dependentStreet;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    @Override
    public String getMainStreet() {
        return mainStreet;
    }

    public List<Voter> getVoters() {
        return voters;
    }
}
