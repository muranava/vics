package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinityworks.webapp.rest.validation.ValidElectorRequest;

import java.util.List;

import static com.infinityworks.webapp.common.lang.ListExtras.isNullOrEmpty;

/**
 * Represents a request to get all voter details in
 * the given wards
 */
@ValidElectorRequest
public final class ElectorPreviewRequest {

    private final List<String> wardNames;
    private final String constituencyName;

    @JsonCreator
    public ElectorPreviewRequest(@JsonProperty("wardNames") List<String> wardNames,
                                 @JsonProperty("constituencyName") String constituencyName) {
        this.wardNames = wardNames;
        this.constituencyName = constituencyName;
    }

    public List<String> getWardNames() {
        return wardNames;
    }

    public String getConstituencyName() {
        return constituencyName;
    }

    @JsonIgnore
    public boolean shouldSearchAllWards() {
        return isNullOrEmpty(wardNames);
    }
}
