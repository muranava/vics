package com.infinityworks.pdfserver.pdf;

import org.springframework.stereotype.Component;

@Component
public class GotvTableConfig implements PdfTableConfig {
    @Override
    public int[] getColumnWidths() {
        return new int[]{
                80, // house
                100, // name
                80, // tel
                0, // likelihood
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
                70  // ERN
        };
    }

    @Override
    public int getNumColumns() {
        return 15;
    }

    @Override
    public boolean showLikelihoodLegend() {
        return false;
    }
}
