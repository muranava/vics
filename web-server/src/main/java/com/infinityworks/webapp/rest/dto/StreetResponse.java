package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreetResponse {
    private final String mainStreet;
    private final String postTown;
    private final String dependentStreet;
    private final String dependentLocality;
    private final Integer numVoters;
    private final Integer numCanvassed;
    private final String priority;

    @JsonCreator
    public StreetResponse(@JsonProperty("mainStreet") String mainStreet,
                          @JsonProperty("postTown") String postTown,
                          @JsonProperty("dependentStreet") String dependentStreet,
                          @JsonProperty("dependentLocality") String dependentLocality,
                          @JsonProperty("numVoters") Integer numVoters,
                          @JsonProperty("numCanvassed") Integer numCanvassed,
                          @JsonProperty("priority") String priority) {
        this.mainStreet = mainStreet;
        this.postTown = postTown;
        this.dependentStreet = dependentStreet;
        this.dependentLocality = dependentLocality;
        this.numVoters = numVoters;
        this.numCanvassed = numCanvassed;
        this.priority = priority;
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

    public Integer getNumVoters() {
        return numVoters;
    }

    public Integer getNumCanvassed() {
        return numCanvassed;
    }

    public String getPriority() {
        return priority;
    }
}
