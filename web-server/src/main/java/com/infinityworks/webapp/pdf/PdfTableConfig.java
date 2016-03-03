package com.infinityworks.webapp.pdf;

public interface PdfTableConfig {
    int[] getColumnWidths();
    int getNumColumns();
    boolean showLikelihoodLegend();
}
