package com.infinityworks.pdfserver.pdf;

import com.infinityworks.pdfserver.pdf.model.Point;
import com.infinityworks.pdfserver.pdf.model.RectPoint;

public class CanvassTableConfig implements PdfTableConfig {
    private static final int[] COLUMN_WIDTHS = new int[]{
            75, // house
            80, // name
            60, // tel
            40, // likelihood
            24, // iss 1
            45, // iss 2
            28, // iss 3
            37, // intention
            15, // has voted
            15, // has pv
            15, // wants pv
            15, // needs lift
            15, // poster
            15, // inaccessible
            15, // dead
            60  // ERN
    };

    @Override
    public int[] getColumnWidths() {
        return COLUMN_WIDTHS;
    }

    @Override
    public int getNumColumns() {
        return COLUMN_WIDTHS.length;
    }

    @Override
    public Point[] footerTextPosition() {
        return new Point[]{Point.of(70, 26), Point.of(70, 15)};
    }

    @Override
    public boolean isGotv() {
        return false;
    }

    @Override
    public Point logoPosition() {
        return Point.of(100, 565);
    }

    @Override
    public RectPoint addressTextPosition() {
        return RectPoint.of(100, 50, 700, 557);
    }

    @Override
    public RectPoint intentionTextPosition() {
        return RectPoint.of(560, 0, 700, 590);
    }
}
