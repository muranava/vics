package com.infinityworks.webapp.autopdfgenerator.dto;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

@Immutable
@Style(init = "with*")
public interface DistrictRow {
    String wardName();
    String wardCode();
    String constituencyName();
    String constituencyCode();
}
