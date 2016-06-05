package com.infinityworks.pdfserver.pdf.model;

public class RectPoint {
    private final float llx;
    private final float lly;
    private final float urx;
    private final float ury;

    private RectPoint(float llx, float lly, float urx, float ury) {
        this.llx = llx;
        this.lly = lly;
        this.urx = urx;
        this.ury = ury;
    }

    public static RectPoint of(float llx, float lly, float urx, float ury) {
        return new RectPoint(llx, lly, urx, ury);
    }

    public float getLowerLeftX() {
        return llx;
    }

    public float getLowerLeftY() {
        return lly;
    }

    public float getUpperRightX() {
        return urx;
    }

    public float getUpperRightY() {
        return ury;
    }
}
