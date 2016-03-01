package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.nullToEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {
    private final String street;
    private final String house;
    private final String postTown;
    private final String postCode;
    private final List<Voter> voters;

    @JsonCreator
    public Property(@JsonProperty("roll_street") String street,
                    @JsonProperty("roll_house") String house,
                    @JsonProperty("post_town") String postTown,
                    @JsonProperty("voters") List<Voter> voters,
                    @JsonProperty("postcode") String postCode) {
        this.street = nullToEmpty(street);
        this.house = nullToEmpty(house);
        this.postCode = nullToEmpty(postCode);
        this.postTown = nullToEmpty(postTown);
        this.voters = firstNonNull(voters, new ArrayList<>());
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public String getPostTown() {
        return postTown;
    }

    public String getPostCode() {
        return postCode;
    }

    public List<Voter> getVoters() {
        return voters;
    }
}
