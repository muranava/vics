package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinityworks.webapp.rest.validation.ValidElectorRequest;

import java.util.List;

/**
 * Represents a request to get all voter details in the given wards
 */
@ValidElectorRequest
public final class ElectorsByWardAndConstituencyRequest {

    private final List<String> wardNames;
    private final String constituencyName;

    @JsonCreator
    public ElectorsByWardAndConstituencyRequest(@JsonProperty("wardNames") List<String> wardNames,
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
}
