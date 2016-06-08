package com.infinityworks.webapp.autopdfgenerator.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

import java.util.List;

@Immutable
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableGeneratePdfRequest.class)
public interface GeneratePdfRequest {
    List<Street> streets();
    Flags flags();
    RequestInfo info();
}
