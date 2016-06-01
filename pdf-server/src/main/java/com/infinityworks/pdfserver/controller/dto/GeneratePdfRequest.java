package com.infinityworks.pdfserver.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneratePdfRequest {
    private final List<StreetRequest> streets;
    private final Flags flags;
    private final RequestInfo info;

    @JsonCreator
    public GeneratePdfRequest(@JsonProperty("streets") List<StreetRequest> streets,
                              @JsonProperty("flags") Flags flags,
                              @JsonProperty("info") RequestInfo info) {
        this.streets = streets;
        this.flags = flags;
        this.info = info;
    }

    public List<StreetRequest> getStreets() {
        return streets;
    }

    public Flags getFlags() {
        return flags;
    }

    public RequestInfo getInfo() {
        return info;
    }
}
