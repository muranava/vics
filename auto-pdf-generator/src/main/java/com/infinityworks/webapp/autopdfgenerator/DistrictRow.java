package com.infinityworks.webapp.autopdfgenerator;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

@Immutable
@Style(init = "with*")
interface DistrictRow {
    String wardName();
    String wardCode();
    String constituencyName();
    String constituencyCode();
}
