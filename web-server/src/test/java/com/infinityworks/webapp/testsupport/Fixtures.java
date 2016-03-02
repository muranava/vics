package com.infinityworks.webapp.testsupport;

import com.infinityworks.webapp.paf.dto.*;
import com.infinityworks.webapp.rest.dto.Street;

public class Fixtures {

    public static ImmutableVoter.Builder voterWithDefaults() {
        return ImmutableVoter.builder()
                .withElectorNumber("PD-123-4")
                .withPollingDistrict("PD")
                .withElectorNumber("123")
                .withElectorSuffix("4")
                .withTelephone("07983441616")
                .withTitle("")
                .withFirstName("Alan")
                .withLastName("Donald")
                .withVoting(votingWithDefaults().build())
                .withFlags(flagsWithDefaults().build())
                .withVolunteer(volunteerWithDefaults().build())
                .withIssues(issuesWithDefaults().build());
    }

    public static ImmutableVoting.Builder votingWithDefaults() {
        return ImmutableVoting.builder()
                .withIntention(3)
                .withLikelihood(4);
    }

    public static ImmutableVolunteer.Builder volunteerWithDefaults() {
        return ImmutableVolunteer.builder()
                .withPoster(false);
    }

    public static ImmutableFlags.Builder flagsWithDefaults() {
        return ImmutableFlags.builder()
                .withDeceased(false)
                .withWantsPV(false)
                .withHasPV(true)
                .withInaccessible(false)
                .withLift(false);
    }

    public static ImmutableIssues.Builder issuesWithDefaults() {
        return ImmutableIssues.builder()
                .withControl(false)
                .withCost(false)
                .withSafety(false);
    }

    public static Street kirbyRoad() {
        return new Street("Kirby Road", "Coventry", "Northern Quarter", "");
    }

    public static Street abbotRoad() {
        return new Street("Abbot Road", "Coventry", "Southern Quarter", "");
    }
}
