package com.infinityworks.pdfserver.pdf;

import com.infinityworks.pdfserver.pdf.model.Point;
import com.infinityworks.pdfserver.pdf.model.RectPoint;
import com.lowagie.text.PageSize;
import org.springframework.stereotype.Component;

@Component
public class GotvTableConfig implements PdfTableConfig {
    private static final int[] COLUMN_WIDTHS = new int[]{
            80, // house
            100, // name
            80, // tel
            37, // intention
            15, // has pv
            15, // needs lift
            15, // inaccessible
            15, // dead
            70  // ERN
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
    public boolean isGotv() {
        return true;
    }

    @Override
    public Point logoPosition() {
        int margin = 35;
        return Point.of(100, PageSize.A4.getHeight() - margin);
    }

    @Override
    public Point[] footerTextPosition() {
        return new Point[]{Point.of(20, 45), Point.of(20, 30)};
    }

    @Override
    public RectPoint addressTextPosition() {
        return RectPoint.of(100, 100, 500, 805);
    }

    @Override
    public RectPoint intentionTextPosition() {
        int topMargin = 10;
        return RectPoint.of(353, 100, 800, PageSize.A4.getHeight() - topMargin);
    }
}
