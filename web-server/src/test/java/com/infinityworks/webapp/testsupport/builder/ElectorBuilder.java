package com.infinityworks.webapp.testsupport.builder;

import com.infinityworks.webapp.domain.Elector;

import java.util.Date;

public class ElectorBuilder {
    private String wardCode;
    private String pollingDistrict;
    private String electorId;
    private String electorSuffix;
    private String title;
    private String firstName;
    private String lastName;
    private String flag;
    private String initial;
    private Date dob;
    private Date modified;
    private Date created;

    public static ElectorBuilder elector() {
        return new ElectorBuilder().withDefaults();
    }

    public ElectorBuilder withDefaults() {
        Date now = new Date();
        withCreated(now);
        withDob(now);
        withElectorId("electorId");
        withElectorSuffix("electorSuffix");
        withFirstName("firstName");
        withLastName("lastName");
        withFlag("F");
        withInitial("A");
        withModified(now);
        withPollingDistrict("pollingDistrict");
        withTitle("title");
        withWardCode("wardCode");
        return this;
    }

    public ElectorBuilder withWardCode(String wardCode) {
        this.wardCode = wardCode;
        return this;
    }

    public ElectorBuilder withPollingDistrict(String pollingDistrict) {
        this.pollingDistrict = pollingDistrict;
        return this;
    }

    public ElectorBuilder withElectorId(String electorId) {
        this.electorId = electorId;
        return this;
    }

    public ElectorBuilder withElectorSuffix(String electorSuffix) {
        this.electorSuffix = electorSuffix;
        return this;
    }

    public ElectorBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ElectorBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ElectorBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ElectorBuilder withFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public ElectorBuilder withInitial(String initial) {
        this.initial = initial;
        return this;
    }

    public ElectorBuilder withDob(Date dob) {
        this.dob = dob;
        return this;
    }

    public ElectorBuilder withModified(Date modified) {
        this.modified = modified;
        return this;
    }

    public ElectorBuilder withCreated(Date created) {
        this.created = created;
        return this;
    }

    public Elector build() {
        return new Elector(wardCode, pollingDistrict, electorId, electorSuffix, title, firstName, lastName, flag, initial, dob, modified, created);
    }
}