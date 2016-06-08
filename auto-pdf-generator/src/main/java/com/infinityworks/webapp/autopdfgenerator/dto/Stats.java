package com.infinityworks.webapp.autopdfgenerator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

@Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Style(init = "with*")
@JsonDeserialize(as = ImmutableStats.class)
@JsonSerialize(as = ImmutableStats.class)
public interface Stats {
    @JsonProperty("voters") int voters();
    @JsonProperty("canvassed") int canvassed();
    @Default @JsonProperty("pledged") default int pledged() {return 0;}
    @Default @JsonProperty("voted_pledges") default int votedPledges() {return 0;}
}
