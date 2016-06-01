package com.infinityworks.pdfserver.testsupport;

import com.infinityworks.pafclient.dto.ImmutableFlags;
import com.infinityworks.pafclient.dto.ImmutableIssues;
import com.infinityworks.pafclient.dto.ImmutableVoter;
import com.infinityworks.pafclient.dto.ImmutableVoting;

public class Fixtures {

    public static ImmutableVoter.Builder voterWithDefaults() {
        return ImmutableVoter.builder()
                .withElectorNumber("PD-123-4")
                .withPollingDistrict("PD")
                .withElectorNumber("123")
                .withElectorSuffix("4")
                .withFullName("Smith, Donald")
                .withVoting(votingWithDefaults().build())
                .withFlags(flagsWithDefaults().build())
                .withIssues(issuesWithDefaults().build());
    }

    public static ImmutableVoting.Builder votingWithDefaults() {
        return ImmutableVoting.builder()
                .withIntention(3)
                .withLikelihood(4);
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
                .withSovereignty(false)
                .withCost(false)
                .withBorder(false);
    }
}
