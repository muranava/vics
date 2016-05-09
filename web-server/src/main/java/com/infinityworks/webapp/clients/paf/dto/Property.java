package com.infinityworks.webapp.clients.paf.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value.Immutable
@Value.Style(init = "with*", jdkOnly = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = ImmutableProperty.class)
@JsonSerialize(as = ImmutableProperty.class)
public interface Property {
     @Value.Default default @JsonProperty("roll_street") String street() {return "";}
     @Value.Default default @JsonProperty("roll_house") String house() {return "";}
     @Value.Default default @JsonProperty("post_town") String postTown() {return "";}
     @Value.Default default @JsonProperty("postcode") String postCode() {return "";}
     @Value.Default default List<Voter> voters() {
        return emptyList();
    }
}
