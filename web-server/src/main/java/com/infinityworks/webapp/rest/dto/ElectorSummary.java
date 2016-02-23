package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ElectorSummary {
    private final String ern;
    private final String firstName;
    private final String lastName;
    private final String address;

    @JsonCreator
    public ElectorSummary(@JsonProperty("ern") String ern,
                          @JsonProperty("firstName") String firstName,
                          @JsonProperty("lastName") String lastName,
                          @JsonProperty("address") String address) {
        this.ern = ern;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getErn() {
        return ern;
    }
}
