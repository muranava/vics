package com.infinityworks.webapp.clients.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutablePafStreetRequest.class)
@JsonSerialize(as = ImmutablePafStreetRequest.class)
public interface PafStreetRequest {
    @Value.Parameter @JsonProperty("main_street") String mainStreet();
    @Value.Parameter @JsonProperty("post_town") String postTown();
    @Value.Parameter @JsonProperty("dependent_street") String dependentStreet();
    @Value.Parameter @JsonProperty("dependent_locality") String dependentLocality();
}
