package com.infinityworks.webapp.autopdfgenerator.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

@Immutable
@Style(init = "with*")
@JsonSerialize(as = ImmutableStreet.class)
public interface Street {
    String mainStreet();
    String postTown();
    String dependentStreet();
    String dependentLocality();
}
