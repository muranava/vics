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
@JsonDeserialize(as = ImmutableInfo.class)
@JsonSerialize(as = ImmutableInfo.class)
public interface Info {
    @Nullable @JsonProperty("phone") String telephone();
    @Nullable @JsonProperty("email") String email();
}
