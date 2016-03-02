package com.infinityworks.webapp.paf.dto;

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
    @Nullable @JsonProperty("polling_district") String pollingDistrict();
    @Nullable @JsonProperty("elector_number") String electorNumber();
    @Nullable @JsonProperty("elector_suffix") String electorSuffix();
    @Nullable @JsonProperty("telephone") String telephone();
    @Nullable @JsonProperty("title") String title();
    @Nullable @JsonProperty("first_name") String firstName();
    @Nullable @JsonProperty("initial") String initial();
    @Nullable @JsonProperty("last_name") String lastName();
    @Nullable @JsonProperty("voting") Voting voting();
    @Nullable @JsonProperty("flags") Flags flags();
    @Nullable @JsonProperty("issues") Issues issues();
    @Nullable @JsonProperty("volunteer") Volunteer volunteer();
}
