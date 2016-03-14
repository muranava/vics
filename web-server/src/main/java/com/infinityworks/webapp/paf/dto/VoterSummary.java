package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableVoterSummary.class)
@JsonSerialize(as = ImmutableVoterSummary.class)
public interface VoterSummary {
    @JsonProperty("ward_code") String wardCode();
    @JsonProperty("prefix") String prefix();
    @JsonProperty("number") String number();
    @JsonProperty("suffix") String suffix();
    @JsonProperty("first_name") String firstName();
    @JsonProperty("surname") String surname();
}
