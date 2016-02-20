package com.infinityworks.testsupport.builder;

import com.infinityworks.commondto.Voter;

public class VoterBuilder {
    private String pollingDistrict;
    private String telephone;
    private String electorId;
    private String electorSuffix;
    private String title;
    private String firstName;
    private String initial;
    private String lastName;

    public static VoterBuilder voter() {
        return new VoterBuilder().withDefaults();
    }

    public VoterBuilder withDefaults() {
        withPollingDistrict("E09211005")
                .withTelephone("07983112613")
                .withElectorId("AB432")
                .withElectorSuffix("")
                .withFirstName("May")
                .withLastName("Rose")
                .withInitial("")
                .withTitle("Ms");
        return this;
    }

    public VoterBuilder withPollingDistrict(String pollingDistrict) {
        this.pollingDistrict = pollingDistrict;
        return this;
    }

    public VoterBuilder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public VoterBuilder withElectorId(String electorId) {
        this.electorId = electorId;
        return this;
    }

    public VoterBuilder withElectorSuffix(String electorSuffix) {
        this.electorSuffix = electorSuffix;
        return this;
    }

    public VoterBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public VoterBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public VoterBuilder withInitial(String initial) {
        this.initial = initial;
        return this;
    }

    public VoterBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Voter build() {
        return new Voter(pollingDistrict, telephone, electorId, electorSuffix, title, firstName, initial, lastName);
    }
}