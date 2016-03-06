package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.emptyList;

@Value.Immutable
@Value.Style(init = "with*", jdkOnly = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = ImmutableProperty.class)
@JsonSerialize(as = ImmutableProperty.class)
public interface Property {
    @Nullable @JsonProperty("roll_street") String street();
    @Nullable @JsonProperty("roll_house") String house();
    @Nullable @JsonProperty("post_town") String postTown();
    @Nullable @JsonProperty("postcode") String postCode();
    @Value.Default default List<Voter> voters() {
        return emptyList();
    }
}
