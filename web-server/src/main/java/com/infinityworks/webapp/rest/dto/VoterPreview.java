package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinityworks.webapp.domain.Ward;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class VoterPreview {
    private final Integer totalRoads;
    private final List<Ward> wards;

    @JsonCreator
    public VoterPreview(@JsonProperty("totalRoads") Integer totalRoads, @JsonProperty("wards") List<Ward> wards) {
        this.totalRoads = totalRoads;
        this.wards = wards;
    }

    public Integer getTotalRoads() {
        return totalRoads;
    }

    public List<Ward> getWards() {
        return wards;
    }
}
