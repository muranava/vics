package com.infinityworks.webapp.autopdfgenerator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

import javax.annotation.Nullable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Immutable
@Value.Style(init = "with*")
@JsonSerialize(as = ImmutableFlags.class)
@JsonInclude(NON_NULL)
public interface Flags {
    @Nullable Integer intentionFrom();
    @Nullable Integer intentionTo();
    @Nullable Integer likelihoodFrom();
    @Nullable Integer likelihoodTo();
    @Nullable Boolean hasPV();
    @Nullable Boolean wantsPv();
    @Nullable Boolean needsLift();
    @Nullable Boolean inaccessible();
    @Nullable Boolean poster();
    @Nullable Boolean telephone();
    @Nullable Boolean email();
}
