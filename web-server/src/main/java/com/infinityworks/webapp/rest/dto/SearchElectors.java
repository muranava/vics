package com.infinityworks.webapp.rest.dto;

import com.google.common.base.MoreObjects;
import com.infinityworks.webapp.rest.validation.ValidSearchElectorsRequest;

import static com.google.common.base.Strings.nullToEmpty;

@ValidSearchElectorsRequest
public class SearchElectors {
    private final String wardCode;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String postCode;

    public SearchElectors(String firstName, String lastName, String address, String postCode, String wardCode) {
        this.firstName = nullToEmpty(firstName);
        this.lastName = nullToEmpty(lastName);
        this.address = nullToEmpty(address);
        this.postCode = nullToEmpty(postCode);
        this.wardCode = nullToEmpty(wardCode);
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

    public String getWardCode() {
        return wardCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("address", address)
                .add("postCode", postCode)
                .add("wardCode", wardCode)
                .toString();
    }
}
