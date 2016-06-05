package com.infinityworks.pdfserver.pdf.model;

public class Point {
    private final float x;
    private final float y;

    private Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(float x, float y) {
        return new Point(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
