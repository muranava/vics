package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Street {
    private final String mainStreet;
    private final String postTown;
    private final String dependentStreet;
    private final String dependentLocality;

    @JsonCreator
    public Street(@JsonProperty("main_street") String mainStreet,
                  @JsonProperty("post_town") String postTown,
                  @JsonProperty("dependent_street") String dependentStreet,
                  @JsonProperty("dependent_locality") String dependentLocality) {
        this.mainStreet = mainStreet;
        this.postTown = postTown;
        this.dependentStreet = dependentStreet;
        this.dependentLocality = dependentLocality;
    }

    public String getMainStreet() {
        return mainStreet;
    }

    public String getPostTown() {
        return postTown;
    }

    public String getDependentStreet() {
        return dependentStreet;
    }

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
