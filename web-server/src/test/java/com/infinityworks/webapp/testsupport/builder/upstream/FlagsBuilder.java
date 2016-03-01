package com.infinityworks.webapp.testsupport.builder.upstream;

import com.infinityworks.webapp.paf.dto.Flags;

public class FlagsBuilder {
    private Boolean hasPV;
    private Boolean wantsPV;
    private Boolean lift;
    private Boolean deceased;
    private Boolean inaccessible;

    public FlagsBuilder withDefaults() {
        withHasPV(true)
                .withDeceased(false)
                .withInaccessible(false)
                .withLift(false)
                .withWantsPV(false);
        return this;
    }

    public static FlagsBuilder flags() {
        return new FlagsBuilder().withDefaults();
    }

    public FlagsBuilder withHasPV(Boolean hasPV) {
        this.hasPV = hasPV;
        return this;
    }

    public FlagsBuilder withWantsPV(Boolean wantsPV) {
        this.wantsPV = wantsPV;
        return this;
    }

    public FlagsBuilder withLift(Boolean lift) {
        this.lift = lift;
        return this;
    }

    public FlagsBuilder withDeceased(Boolean deceased) {
        this.deceased = deceased;
        return this;
    }

    public FlagsBuilder withInaccessible(Boolean inaccessible) {
        this.inaccessible = inaccessible;
        return this;
    }

    public Flags build() {
        return new Flags(hasPV, wantsPV, lift, deceased, inaccessible);
    }
}