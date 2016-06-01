package com.infinityworks.pafclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableVoterAddress.class)
@JsonSerialize(as = ImmutableVoterAddress.class)
public interface VoterAddress {
    @Nullable Integer udprn();
    @Nullable @JsonProperty("line_1") String addressLine1();
    @Nullable @JsonProperty("line_2") String addressLine2();
    @Nullable @JsonProperty("post_town") String postTown();
    @Nullable String postcode();
}
