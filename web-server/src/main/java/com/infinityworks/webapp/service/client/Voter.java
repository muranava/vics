package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Voter {
    private final String wardCode;
    private final String pollingDistrict;
    private final String electorId;
    private final String electorSuffix;
    private final String title;
    private final String firstName;
    private final String lastName;
    private final String initial;
    private final String house;
    private final String street;

    public Voter(@JsonProperty("wardCode") String wardCode,
                 @JsonProperty("pollingDistrict") String pollingDistrict,
                 @JsonProperty("electorId") String electorId,
                 @JsonProperty("electorSuffix") String electorSuffix,
                 @JsonProperty("title") String title,
                 @JsonProperty("firstName") String firstName,
                 @JsonProperty("lastName") String lastName,
                 @JsonProperty("initial") String initial,
                 @JsonProperty("house") String house,
                 @JsonProperty("street") String street) {

        this.wardCode = wardCode;
        this.pollingDistrict = pollingDistrict;
        this.electorId = electorId;
        this.electorSuffix = electorSuffix;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.initial = initial;
        this.house = house;
        this.street = street;
    }

    public String getWardCode() {
        return wardCode;
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

    public String getLastName() {
        return lastName;
    }

    public String getInitial() {
        return initial;
    }

    public String getHouse() {
        return house;
    }

    public String getStreet() {
        return street;
    }
}
