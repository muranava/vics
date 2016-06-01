package com.infinityworks.pdfserver.pdf;

public interface PdfTableConfig {
    int[] getColumnWidths();
    int getNumColumns();
    boolean showLikelihoodLegend();
}
