package com.infinityworks.webapp.clients.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*", jdkOnly = true)
@JsonDeserialize(as = ImmutableStreetsResponse.class)
@JsonSerialize(as = ImmutableStreetsResponse.class)
public interface StreetsResponse {
    @JsonProperty("response") List<PafStreetResponse> response();
    @JsonProperty("stats") Stats stats();
}
