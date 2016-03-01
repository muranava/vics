package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import static com.google.common.base.Strings.nullToEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Voter {

    private final String pollingDistrict;
    private final String electorNumber;
    private final String electorSuffix;
    private final String telephone;
    private final String title;
    private final String firstName;
    private final String initial;
    private final String lastName;
    private final Voting voting;
    private final Flags flags;
    private final Issues issues;
    private final Volunteer volunteer;

    @JsonCreator
    public Voter(
            @JsonProperty("polling_district") String pollingDistrict,
            @JsonProperty("elector_number") String electorNumber,
            @JsonProperty("elector_suffix") String electorSuffix,
            @JsonProperty("telephone") String telephone,
            @JsonProperty("title") String title,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("initial") String initial,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("voting") Voting voting,
            @JsonProperty("flags") Flags flags,
            @JsonProperty("issues") Issues issues,
            @JsonProperty("volunteer") Volunteer volunteer) {

        this.pollingDistrict = pollingDistrict;
        this.electorNumber = electorNumber;
        this.electorSuffix = electorSuffix;
        this.telephone = telephone;
        this.title = title;
        this.firstName = firstName;
        this.initial = initial;
        this.lastName = lastName;
        this.voting = voting;
        this.flags = flags;
        this.issues = issues;
        this.volunteer = volunteer;
    }

    public String getPollingDistrict() {
        return pollingDistrict;
    }

    public String getElectorNumber() {
        return electorNumber;
    }

    public String getElectorSuffix() {
        return electorSuffix;
    }

    public String getTelephone() {
        return telephone;
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

    public Voting getVoting() {
        return voting;
    }

    public Flags getFlags() {
        return flags;
    }

    public Issues getIssues() {
        return issues;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }
}
