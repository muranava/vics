package com.infinityworks.testsupport.builder;

import com.infinityworks.commondto.Flags;

public class FlagsBuilder {
    private Integer intentionFrom;
    private Integer intentionTo;
    private Integer likelihoodFrom;
    private Integer likelihoodTo;
    private Boolean hasPV;
    private Boolean wantsPv;
    private Boolean needsLift;
    private Boolean notCanvassedYet;

    public static FlagsBuilder flags() {
        return new FlagsBuilder().withDefaults();
    }

    public FlagsBuilder withDefaults() {
        return this;
    }

    public FlagsBuilder withIntentionFrom(Integer intentionFrom) {
        this.intentionFrom = intentionFrom;
        return this;
    }

    public FlagsBuilder withIntentionTo(Integer intentionTo) {
        this.intentionTo = intentionTo;
        return this;
    }

    public FlagsBuilder withLikelihoodFrom(Integer likelihoodFrom) {
        this.likelihoodFrom = likelihoodFrom;
        return this;
    }

    public FlagsBuilder withLikelihoodTo(Integer likelihoodTo) {
        this.likelihoodTo = likelihoodTo;
        return this;
    }

    public FlagsBuilder withHasPV(Boolean hasPV) {
        this.hasPV = hasPV;
        return this;
    }

    public FlagsBuilder withWantsPv(Boolean wantsPv) {
        this.wantsPv = wantsPv;
        return this;
    }

    public FlagsBuilder withNeedsLift(Boolean needsLift) {
        this.needsLift = needsLift;
        return this;
    }

    public FlagsBuilder withNotCanvassedYet(Boolean notCanvassedYet) {
        this.notCanvassedYet = notCanvassedYet;
        return this;
    }

    public Flags build() {
        return new Flags(intentionFrom, intentionTo, likelihoodFrom, likelihoodTo, hasPV, wantsPv, needsLift, notCanvassedYet);
    }
}