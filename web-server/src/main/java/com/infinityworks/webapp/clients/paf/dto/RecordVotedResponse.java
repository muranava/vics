package com.infinityworks.webapp.clients.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

// TODO this is a placeholder and will change when the API is defined
@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableRecordVotedResponse.class)
@JsonSerialize(as = ImmutableRecordVotedResponse.class)
public interface RecordVotedResponse {
    @JsonProperty("success") Boolean success();
}
