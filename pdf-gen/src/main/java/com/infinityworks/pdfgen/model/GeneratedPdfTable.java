package com.infinityworks.pdfgen.model;

import com.lowagie.text.pdf.PdfPTable;

public class GeneratedPdfTable {
    private final PdfPTable table;
    private final String mainStreetName;
    private final String wardName;
    private final String wardCode;
    private final String constituencyName;

    public GeneratedPdfTable(PdfPTable table, String mainStreetName, String wardName, String wardCode, String constituencyName) {
        this.table = table;
        this.mainStreetName = mainStreetName;
        this.wardName = wardName;
        this.wardCode = wardCode;
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

    public String getWardCode() {
        return wardCode;
    }
}
