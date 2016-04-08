package com.infinityworks.webapp.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.infinityworks.webapp.converter.ErnSerializer;

@JsonSerialize(using = ErnSerializer.class)
public class Ern {
    private static final String ERN_REGEX = "^\\w{1,10}-\\w{1,7}-\\d{1,7}-\\w{1,2}$";

    private final String wardCode;
    private final String pollingDistrict;
    private final String number;
    private final String suffix;

    public Ern(String wardCode, String pollingDistrict, String number, String suffix) {
        this.wardCode = wardCode;
        this.pollingDistrict = pollingDistrict;
        this.number = number;
        this.suffix = suffix;
    }

    public static Ern valueOf(String value) {
        if (!value.matches(ERN_REGEX)) {
            throw new IllegalArgumentException("Invalid ERN format");
        } else {
            String[] parts = value.split("-");
            return new Ern(parts[0], parts[1], parts[2], parts[3]);
        }
    }

    public String getPollingDistrict() {
        return pollingDistrict;
    }

    public String getWardCode() {
        return wardCode;
    }

    public String getNumber() {
        return number;
    }

    public String getSuffix() {
        return suffix;
    }

    public String get() {
        return getWardCode() + "-" + getPollingDistrict() + "-" + getNumber() + "-" + getSuffix();
    }
}
