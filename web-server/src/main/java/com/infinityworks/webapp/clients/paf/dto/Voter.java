package com.infinityworks.webapp.clients.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableVoter.class)
@JsonSerialize(as = ImmutableVoter.class)
public interface Voter {
    @Nullable @JsonProperty("prefix") String pollingDistrict();
    @Nullable @JsonProperty("number") String electorNumber();
    @Nullable @JsonProperty("suffix") String electorSuffix();
    @Nullable @JsonProperty("full_name") String fullName();
    @Nullable @JsonProperty("voting") Voting voting();
    @Nullable @JsonProperty("flags") Flags flags();
    @Nullable @JsonProperty("issues") Issues issues();
    @Nullable @JsonProperty("volunteer") Volunteer volunteer();
    @Nullable @JsonProperty("info") Info info();
}
