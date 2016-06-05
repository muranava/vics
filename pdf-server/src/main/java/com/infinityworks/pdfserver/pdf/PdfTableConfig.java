package com.infinityworks.pdfserver.pdf;

import com.infinityworks.pdfserver.pdf.model.Point;
import com.infinityworks.pdfserver.pdf.model.RectPoint;

public interface PdfTableConfig {
    int[] getColumnWidths();
    int getNumColumns();
    boolean isGotv();
    Point logoPosition();
    Point[] footerTextPosition();
    RectPoint addressTextPosition();
    RectPoint intentionTextPosition();
}
