package com.infinityworks.webapp.clients.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutablePafStreetResponse.class)
@JsonSerialize(as = ImmutablePafStreetResponse.class)
public interface PafStreetResponse {
    @JsonProperty("main_street") String mainStreet();
    @JsonProperty("post_town") String postTown();
    @JsonProperty("dependent_street") String dependentStreet();
    @JsonProperty("dependent_locality") String dependentLocality();
    @JsonProperty("canvassed") Integer canvassed();
    @JsonProperty("voters") Integer voters();
    @JsonProperty("priority") @Value.Default default String priority() {
        return "MED";
    }
}
