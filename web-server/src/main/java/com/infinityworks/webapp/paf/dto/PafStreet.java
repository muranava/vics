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
@JsonDeserialize(as = ImmutablePafStreet.class)
@JsonSerialize(as = ImmutablePafStreet.class)
public interface PafStreet {
    @Value.Parameter @Nullable @JsonProperty("main_street") String mainStreet();
    @Value.Parameter @Nullable @JsonProperty("post_town") String postTown();
    @Value.Parameter @Nullable @JsonProperty("dependent_street") String dependentStreet();
    @Value.Parameter @Nullable @JsonProperty("dependent_locality") String dependentLocality();
}
