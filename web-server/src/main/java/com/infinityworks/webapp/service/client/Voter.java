package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Voter {
    private final String pollingDistrict;
    private final String electorId;
    private final String electorSuffix;
    private final String title;
    private final String firstName;
    private final String initial;
    private final String lastName;

    @JsonCreator
    public Voter(@JsonProperty("polling_district") String pollingDistrict,
                 @JsonProperty("elector_id") String electorId,
                 @JsonProperty("elector_suffix") String electorSuffix,
                 @JsonProperty("title") String title,
                 @JsonProperty("first_name") String firstName,
                 @JsonProperty("initial") String initial,
                 @JsonProperty("last_name") String lastName) {
        this.pollingDistrict = pollingDistrict;
        this.electorId = electorId;
        this.electorSuffix = electorSuffix;
        this.title = title;
        this.firstName = firstName;
        this.initial = initial;
        this.lastName = lastName;
    }

    public String getPollingDistrict() {
        return pollingDistrict;
    }

    public String getElectorId() {
        return electorId;
    }

    public String getElectorSuffix() {
        return electorSuffix;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getInitial() {
        return initial;
    }

    public String getLastName() {
        return lastName;
    }
}
