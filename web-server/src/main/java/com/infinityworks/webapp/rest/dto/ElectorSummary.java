package com.infinityworks.webapp.rest.dto;

public class ElectorSummary {
    private final String ern;
    private final String firstName;
    private final String lastName;
    private final String address;

    public ElectorSummary(String ern, String firstName,
                          String lastName, String address) {
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
