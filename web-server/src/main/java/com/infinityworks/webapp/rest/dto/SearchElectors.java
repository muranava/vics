package com.infinityworks.webapp.rest.dto;

import com.google.common.base.MoreObjects;
import com.infinityworks.webapp.rest.validation.ValidSearchElectorsRequest;

import static com.google.common.base.Strings.nullToEmpty;

@ValidSearchElectorsRequest
public class SearchElectors {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String postCode;

    public SearchElectors(String firstName, String lastName, String address, String postCode) {
        this.firstName = nullToEmpty(firstName);
        this.lastName = nullToEmpty(lastName);
        this.address = nullToEmpty(address);
        this.postCode = nullToEmpty(postCode);
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

    public String getPostCode() {
        return postCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("address", address)
                .add("postCode", postCode)
                .toString();
    }
}
