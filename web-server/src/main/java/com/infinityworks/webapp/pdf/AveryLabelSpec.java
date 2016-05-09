package com.infinityworks.webapp.pdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableAveryLabelSpec.class)
@JsonSerialize(as = ImmutableAveryLabelSpec.class)
public interface AveryLabelSpec {
    float labelHeightMillis();
    float labelWidthMillis();
    float leftRightMarginMillis();
    float topBottomMarginMillis();
    float labelGapMillis();
}
