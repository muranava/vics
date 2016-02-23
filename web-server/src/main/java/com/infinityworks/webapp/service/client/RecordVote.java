package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class RecordVote {
    private final String wardCode;
    private final String wardName;
    private final String ern;
    private final Boolean success;

    @JsonCreator
    public RecordVote(@JsonProperty("wardCode") String wardCode,
                      @JsonProperty("wardName") String wardName,
                      @JsonProperty("ern") String ern,
                      @JsonProperty("success") Boolean success) {
        this.wardCode = wardCode;
        this.wardName = wardName;
        this.ern = ern;
        this.success = success != null ? success : false;
    }

    public String getWardName() {
        return wardName;
    }

    public String getWardCode() {
        return wardCode;
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
                .add("wardName", wardName)
                .add("ern", ern)
                .add("success", success)
                .toString();
    }
}
