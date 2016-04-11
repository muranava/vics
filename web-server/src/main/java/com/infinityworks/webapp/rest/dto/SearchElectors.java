package com.infinityworks.webapp.rest.dto;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.infinityworks.webapp.rest.validation.ValidSearchElectorsRequest;

import java.util.Map;

@ValidSearchElectorsRequest
public class SearchElectors {
    private final String surname;
    private final String postcode;
    private final String wardCode;
    private final int limit;

    public SearchElectors(String surname, String postcode, String wardCode, int limit) {
        this.surname =  surname;
        this.postcode = postcode;
        this.wardCode = wardCode;
        this.limit = limit;
    }

    public String getWardCode() {
        return wardCode;
    }

    public String getSurname() {
        return surname;
    }

    public String getPostcode() {
        return postcode;
    }

    public int getLimit() {
        return limit;
    }

    /**
     * Converts to upstream request format
     */
    public Map<String, String> getParameters() {
        return ImmutableMap.<String, String>builder()
                .put("ward_code", wardCode)
                .put("postcode", postcode)
                .put("surname", surname)
                .build();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wardCode", wardCode)
                .add("surname", surname)
                .add("postcode", postcode)
                .toString();
    }
}
