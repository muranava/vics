package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Street {
    private final String mainStreet;
    private final String postTown;
    private final String dependentStreet;
    private final String dependentLocality;
    private final Integer numVoters;
    private final Integer numCanvassed;
    private final Object postcode;
    private final Integer priority;

    @JsonCreator
    public Street(@JsonProperty("mainStreet") String mainStreet,
                  @JsonProperty("postTown") String postTown,
                  @JsonProperty("dependentStreet") String dependentStreet,
                  @JsonProperty("dependentLocality") String dependentLocality,
                  @JsonProperty("numVoters") Integer numVoters,
                  @JsonProperty("numCanvassed") Integer numCanvassed,
                  @JsonProperty("postcode") Object postcode,
                  @JsonProperty("priority") Integer priority) {
        this.mainStreet = mainStreet;
        this.postTown = postTown;
        this.dependentStreet = dependentStreet;
        this.dependentLocality = dependentLocality;
        this.numVoters = numVoters;
        this.numCanvassed = numCanvassed;
        this.postcode = postcode;
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

    public Object getPostcode() {
        return postcode;
    }

    public Integer getPriority() {
        return priority;
    }
}
