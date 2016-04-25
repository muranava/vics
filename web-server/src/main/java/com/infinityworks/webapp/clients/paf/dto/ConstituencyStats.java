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
    @Value.Default default @JsonProperty("voters") int voters() {return 0;}
    @Value.Default default @JsonProperty("canvassed") int canvassed() {return 0;}
    @Value.Default default @JsonProperty("voted") int voted() {return 0;}
    @Value.Default default @JsonProperty("pledged") int pledged() {return 0;}
    @Value.Default default @JsonProperty("voted_pledges") int votedPledges() {return 0;}
}
