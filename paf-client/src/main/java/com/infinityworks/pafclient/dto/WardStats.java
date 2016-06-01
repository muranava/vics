package com.infinityworks.pafclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableWardStats.class)
@JsonSerialize(as = ImmutableWardStats.class)
public interface WardStats {
    @JsonProperty("voters") int voters();
    @JsonProperty("canvassed") int canvassed();
    @JsonProperty("pledged") int pledged();
    @JsonProperty("voted") StatsVoted voted();
    @JsonProperty("intention") StatsIntention intention();
}
