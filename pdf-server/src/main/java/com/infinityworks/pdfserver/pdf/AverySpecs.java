package com.infinityworks.pdfserver.pdf;

public class AverySpecs {
    private static final float MILLIS_TO_POINTS_CONVERSION = 2.83464f;

    public static AveryLabelSpec a4() {
        return ImmutableAveryLabelSpec.builder()
                .withLabelGapMillis(4.318f * MILLIS_TO_POINTS_CONVERSION)
                .withLabelHeightMillis(38.1f * MILLIS_TO_POINTS_CONVERSION)
                .withLabelWidthMillis(63.5f * MILLIS_TO_POINTS_CONVERSION)
                .withLeftRightMarginMillis(7f * MILLIS_TO_POINTS_CONVERSION)
                .withTopBottomMarginMillis(15f * MILLIS_TO_POINTS_CONVERSION)
                .build();
    }
}
