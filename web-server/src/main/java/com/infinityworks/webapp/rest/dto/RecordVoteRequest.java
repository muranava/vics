package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.hibernate.validator.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordVoteRequest {
    @NotEmpty
    private final String wardCode;
    @NotEmpty
    private final String wardName;
    @NotEmpty
    private final String ern;
    private final Boolean success;

    @JsonCreator
    public RecordVoteRequest(@JsonProperty("wardCode") String wardCode,
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecordVoteRequest)) return false;
        RecordVoteRequest that = (RecordVoteRequest) o;
        return Objects.equal(wardCode, that.wardCode) &&
                Objects.equal(wardName, that.wardName) &&
                Objects.equal(ern, that.ern) &&
                Objects.equal(success, that.success);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(wardCode, wardName, ern, success);
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
