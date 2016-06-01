package com.infinityworks.pdfserver.pdf;

public class CanvassTableConfig implements PdfTableConfig {
    @Override
    public int[] getColumnWidths() {
        return new int[] {
                75, // house
                85, // name
                70, // tel
                40, // likelihood
                24, // iss 1
                45, // iss 2
                28, // iss 3
                37, // intention
                15, // has pv
                15, // wants pv
                15, // needs lift
                15, // poster
                15, // inaccessible
                15, // dead
                60  // ERN
        };
    }

    @Override
    public int getNumColumns() {
        return 15;
    }

    @Override
    public boolean showLikelihoodLegend() {
        return true;
    }
}
