package com.infinityworks.pdfserver.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestInfo {
    private final String wardCode;
    private final String wardName;
    private final String constituencyName;

    @JsonCreator
    public RequestInfo(@JsonProperty("wardCode") String wardCode,
                       @JsonProperty("wardName") String wardName,
                       @JsonProperty("constituencyName") String constituencyName) {
        this.wardCode = wardCode;
        this.wardName = wardName;
        this.constituencyName = constituencyName;
    }

    public String getWardCode() {
        return wardCode;
    }

    public String getWardName() {
        return wardName;
    }

    public String getConstituencyName() {
        return constituencyName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wardCode", wardCode)
                .add("wardName", wardName)
                .add("constituencyName", constituencyName)
                .toString();
    }
}
