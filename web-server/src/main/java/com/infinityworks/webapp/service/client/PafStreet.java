package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class PafStreet {
    private final String mainStreet;
    private final String postTown;
    private final String dependentStreet;
    private final String dependentLocality;

    @JsonCreator
    public PafStreet(@JsonProperty("main_street") String mainStreet,
                  @JsonProperty("post_town") String postTown,
                  @JsonProperty("dependent_street") String dependentStreet,
                  @JsonProperty("dependent_locality") String dependentLocality) {
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

