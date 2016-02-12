package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class StreetRequest {
    private final String mainStreet;
    private final String postTown;
    private final String dependentStreet;
    private final String dependentLocality;

    public StreetRequest(String mainStreet,
                         String postTown,
                         String dependentStreet,
                         String dependentLocality) {
        this.mainStreet = mainStreet;
        this.postTown = postTown;
        this.dependentStreet = dependentStreet;
        this.dependentLocality = dependentLocality;
    }

    @JsonProperty("main_street")
    public String getMainStreet() {
        return mainStreet;
    }

    @JsonProperty("post_town")
    public String getPostTown() {
        return postTown;
    }

    @JsonProperty("dependent_street")
    public String getDependentStreet() {
        return dependentStreet;
    }

    @JsonProperty("dependent_locality")
    public String getDependentLocality() {
        return dependentLocality;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mainStreet", mainStreet)
                .add("postTown", postTown)
                .add("dependentStreet", dependentStreet)
                .add("dependentLocality", dependentLocality)
                .toString();
    }
}

