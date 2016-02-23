package com.infinityworks.pdfgen.model;

public class ElectorRow {
    private final String house;
    private final String street;
    private final String name;
    private final String telephone;
    private final String likelihood;
    private final String issue1;
    private final String issue2;
    private final String issue3;
    private final String support;
    private final String hasPV;
    private final String wantsPV;
    private final String needsLift;
    private final String poster;
    private final String deceased;
    private final String ern;


    public ElectorRow(String house, String street, String name, String telephone, String likelihood, String issue1, String issue2, String issue3, String support, String hasPV, String wantsPV, String needsLift, String poster, String deceased, String ern) {
        this.house = house;
        this.street = street;
        this.name = name;
        this.telephone = telephone;
        this.likelihood = likelihood;
        this.issue1 = issue1;
        this.issue2 = issue2;
        this.issue3 = issue3;
        this.support = support;
        this.hasPV = hasPV;
        this.wantsPV = wantsPV;
        this.needsLift = needsLift;
        this.poster = poster;
        this.deceased = deceased;
        this.ern = ern;
    }

    public String getHouse() {
        return house;
    }

    public String getStreet() {
        return street;
    }

    public String getName() {
        return name;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getLikelihood() {
        return likelihood;
    }

    public String getIssue1() {
        return issue1;
    }

    public String getIssue2() {
        return issue2;
    }

    public String getIssue3() {
        return issue3;
    }

    public String getSupport() {
        return support;
    }

    public String getHasPV() {
        return hasPV;
    }

    public String getWantsPV() {
        return wantsPV;
    }

    public String getNeedsLift() {
        return needsLift;
    }

    public String getPoster() {
        return poster;
    }

    public String getDeceased() {
        return deceased;
    }

    public String getErn() {
        return ern;
    }
}
