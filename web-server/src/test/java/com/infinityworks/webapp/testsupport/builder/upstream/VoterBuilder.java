package com.infinityworks.webapp.testsupport.builder.upstream;

import com.infinityworks.webapp.paf.dto.*;

public class VoterBuilder {
    private String pollingDistrict;
    private String electorNumber;
    private String electorSuffix;
    private String telephone;
    private String title;
    private String firstName;
    private String initial;
    private String lastName;
    private Voting voting;
    private Flags flags;
    private Issues issues;
    private Volunteer volunteer;

    public VoterBuilder withDefaults() {
        ImmutableFlags flags = ImmutableFlags.builder()
                .withDeceased(false)
                .withWantsPV(false)
                .withHasPV(true)
                .withInaccessible(false)
                .withLift(false)
                .build();

        ImmutableIssues issues = ImmutableIssues.builder()
                .withControl(false)
                .withCost(false)
                .withSafety(false)
                .build();

        withElectorNumber("PD-123-4")
                .withPollingDistrict("PD")
                .withElectorNumber("123")
                .withElectorSuffix("4")
                .withTelephone("07983441616")
                .withTitle("")
                .withFirstName("Alan")
                .withLastName("Donald")
                .withFlags(flags)
                .withIssues(issues);

        return this;
    }

    public static VoterBuilder voter() {
        return new VoterBuilder().withDefaults();
    }

    public VoterBuilder withPollingDistrict(String pollingDistrict) {
        this.pollingDistrict = pollingDistrict;
        return this;
    }

    public VoterBuilder withElectorNumber(String electorNumber) {
        this.electorNumber = electorNumber;
        return this;
    }

    public VoterBuilder withElectorSuffix(String electorSuffix) {
        this.electorSuffix = electorSuffix;
        return this;
    }

    public VoterBuilder withTelephone(String telephone) {
        this.telephone = telephone;
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

    public VoterBuilder withVoting(Voting voting) {
        this.voting = voting;
        return this;
    }

    public VoterBuilder withFlags(Flags flags) {
        this.flags = flags;
        return this;
    }

    public VoterBuilder withIssues(Issues issues) {
        this.issues = issues;
        return this;
    }

    public VoterBuilder withVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
        return this;
    }

    public Voter build() {
        return new Voter(pollingDistrict, electorNumber, electorSuffix, telephone, title, firstName, initial, lastName, voting, flags, issues, volunteer);
    }
}