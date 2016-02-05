package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ElectorsByWardsRequest {
    private final List<String> wardCodes;

    @JsonCreator
    public ElectorsByWardsRequest(@JsonProperty("wardCodes") List<String> wardCodes) {
        this.wardCodes = wardCodes;
    }

    public List<String> getWardCodes() {
        return wardCodes;
    }
}
