package com.infinityworks.pdfgen.model;

import com.itextpdf.text.pdf.PdfPTable;

public class GeneratedPdfTable {
    private final PdfPTable table;
    private final String mainStreetName;
    private final String wardName;
    private final String constituencyName;

    public GeneratedPdfTable(PdfPTable table, String mainStreetName, String wardName, String constituencyName) {
        this.table = table;
        this.mainStreetName = mainStreetName;
        this.wardName = wardName;
        this.constituencyName = constituencyName;
    }

    public PdfPTable getTable() {
        return table;
    }

    public String getMainStreetName() {
        return mainStreetName;
    }

    public String getWardName() {
        return wardName;
    }

    public String getConstituencyName() {
        return constituencyName;
    }
}
