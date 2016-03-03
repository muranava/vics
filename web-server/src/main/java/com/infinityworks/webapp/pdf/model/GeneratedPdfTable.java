package com.infinityworks.webapp.pdf.model;

import com.lowagie.text.pdf.PdfPTable;

public class GeneratedPdfTable {
    private final PdfPTable table;
    private final String street;
    private final String wardName;
    private final String wardCode;
    private final String constituencyName;

    public GeneratedPdfTable(PdfPTable table, String street, String wardName, String wardCode, String constituencyName) {
        this.table = table;
        this.street = street;
        this.wardName = wardName;
        this.wardCode = wardCode;
        this.constituencyName = constituencyName;
    }

    public PdfPTable getTable() {
        return table;
    }

    public String getStreet() {
        return street;
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
