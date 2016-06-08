package com.infinityworks.webapp.autopdfgenerator.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

@Immutable
@Value.Style(init = "with*")
@JsonSerialize(as = ImmutableRequestInfo.class)
public interface RequestInfo {
    String wardCode();
    String wardName();
    String constituencyName();
}
