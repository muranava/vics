package com.infinityworks.webapp.clients.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableSearchVoterResponse.class)
@JsonSerialize(as = ImmutableSearchVoterResponse.class)
public interface SearchVoterResponse {
    @JsonProperty("full_name") String fullName();
    @JsonProperty("first_name") String firstName();
    @JsonProperty("surname") String surname();
    @JsonProperty("ern") String ern();
    @JsonProperty("address") VoterAddress address();
}
