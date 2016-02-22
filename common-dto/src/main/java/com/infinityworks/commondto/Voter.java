package com.infinityworks.commondto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Strings.nullToEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Voter {
    private final String ern;
    private final String pollingDistrict;
    private final String telephone;
    private final String electorId;
    private final String electorSuffix;
    private final String title;
    private final String firstName;
    private final String initial;
    private final String lastName;

    @JsonCreator
    public Voter(
            @JsonProperty("ern") String ern,
            @JsonProperty("polling_district") String pollingDistrict,
            @JsonProperty("telephone") String telephone,
            @JsonProperty("elector_id") String electorId,
            @JsonProperty("elector_suffix") String electorSuffix,
            @JsonProperty("title") String title,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("initial") String initial,
            @JsonProperty("last_name") String lastName) {
        this.ern = ern;
        this.pollingDistrict = nullToEmpty(pollingDistrict);
        this.telephone = nullToEmpty(telephone);
        this.electorId = nullToEmpty(electorId);
        this.electorSuffix = nullToEmpty(electorSuffix);
        this.title = nullToEmpty(title);
        this.firstName = nullToEmpty(firstName);
        this.initial = nullToEmpty(initial);
        this.lastName = nullToEmpty(lastName);
    }

    public String getErn() {
        return ern;
    }

    public String getTelephone() {
        return telephone;
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
