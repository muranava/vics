package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.infinityworks.webapp.clients.paf.dto.Stats;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonSerialize(as = ImmutableStreetsByWardResponse.class)
@JsonDeserialize(as = ImmutableStreetsByWardResponse.class)
public interface StreetsByWardResponse {
    List<Street> streets();
    Stats stats();
}
