package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class RecordVote {
    private final String wardCode;
    private final String pollingDistrict;
    private final String ern;
    private final Boolean success;

    @JsonCreator
    public RecordVote(@JsonProperty("wardCode") String wardCode,
                      @JsonProperty("pollingDistrict") String pollingDistrict,
                      @JsonProperty("ern") String ern,
                      @JsonProperty("success") Boolean success) {
        this.wardCode = wardCode;
        this.pollingDistrict = pollingDistrict;
        this.ern = ern;
        this.success = success != null ? success : false;
    }

    public String getWardCode() {
        return wardCode;
    }

    public String getPollingDistrict() {
        return pollingDistrict;
    }

    public String getErn() {
        return ern;
    }

    public Boolean getSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wardCode", wardCode)
                .add("pollingDistrict", pollingDistrict)
                .add("ern", ern)
                .add("success", success)
                .toString();
    }
}
