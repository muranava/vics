package com.infinityworks.pdfgen;

public class ElectorRow {
    private final String house;
    private final String name;
    private final String ern;

    public ElectorRow(String house, String name, String ern) {
        this.house = house;
        this.name = name;
        this.ern = ern;
    }

    public String getHouse() {
        return house;
    }

    public String getName() {
        return name;
    }

    public String getErn() {
        return ern;
    }
}
