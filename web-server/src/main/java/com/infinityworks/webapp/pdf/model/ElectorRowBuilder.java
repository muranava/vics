package com.infinityworks.webapp.pdf.model;

public class ElectorRowBuilder {
    private String house = "";
    private String street = "";
    private String name = "";
    private String telephone = "";
    private String likelihood = "";
    private String issue1 = "";
    private String issue2 = "";
    private String issue3 = "";
    private String support = "";
    private String hasPV = "";
    private String wantsPV = "";
    private String needsLift = "";
    private String poster = "";
    private String deceased = "";
    private String ern = "";
    private String inaccessible = "";
    private String email = "";

    public static ElectorRowBuilder electorRow() {
        return new ElectorRowBuilder();
    }

    public ElectorRowBuilder withHouse(String house) {
        this.house = house;
        return this;
    }

    public ElectorRowBuilder withStreet(String street) {
        this.street = street;
        return this;
    }

    public ElectorRowBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ElectorRowBuilder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public ElectorRowBuilder withLikelihood(String likelihood) {
        this.likelihood = likelihood;
        return this;
    }

    public ElectorRowBuilder withIssue1(String issue1) {
        this.issue1 = issue1;
        return this;
    }

    public ElectorRowBuilder withIssue2(String issue2) {
        this.issue2 = issue2;
        return this;
    }

    public ElectorRowBuilder withIssue3(String issue3) {
        this.issue3 = issue3;
        return this;
    }

    public ElectorRowBuilder withIntention(String support) {
        this.support = support;
        return this;
    }

    public ElectorRowBuilder withHasPV(String hasPV) {
        this.hasPV = hasPV;
        return this;
    }

    public ElectorRowBuilder withWantsPV(String wantsPV) {
        this.wantsPV = wantsPV;
        return this;
    }

    public ElectorRowBuilder withNeedsLift(String needsLift) {
        this.needsLift = needsLift;
        return this;
    }

    public ElectorRowBuilder withPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public ElectorRowBuilder withDeceased(String deceased) {
        this.deceased = deceased;
        return this;
    }

    public ElectorRowBuilder withErn(String ern) {
        this.ern = ern;
        return this;
    }

    public ElectorRowBuilder withInaccessible(String inaccessible) {
        this.inaccessible = inaccessible;
        return this;
    }

    public ElectorRowBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ElectorRow build() {
        return new ElectorRow(house, street, name, telephone, likelihood, issue1, issue2, issue3, support, hasPV, wantsPV, needsLift, poster, deceased, ern, inaccessible, email);
    }
}