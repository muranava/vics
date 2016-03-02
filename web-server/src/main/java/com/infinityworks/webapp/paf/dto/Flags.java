package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableFlags.class)
@JsonSerialize(as = ImmutableFlags.class)
public interface Flags {
    @Nullable @JsonProperty("has_postal") Boolean hasPV();
    @Nullable @JsonProperty("wants_postal") Boolean wantsPV();
    @Nullable Boolean lift();
    @Nullable Boolean deceased();
    @Nullable Boolean inaccessible();
}
