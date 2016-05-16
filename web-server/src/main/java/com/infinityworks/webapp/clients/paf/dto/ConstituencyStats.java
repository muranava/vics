package com.infinityworks.webapp.clients.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableConstituencyStats.class)
@JsonSerialize(as = ImmutableConstituencyStats.class)
public interface ConstituencyStats {
    @JsonProperty("voters") int voters();
    @JsonProperty("canvassed") int canvassed();
    @JsonProperty("pledged") int pledged();
    @JsonProperty("voted") StatsVoted voted();
    @JsonProperty("intention") StatsIntention intention();
}
