package com.infinityworks.webapp.testsupport.builder;

import com.infinityworks.webapp.rest.dto.RecordContactRequest;

public class RecordContactRequestBuilder {
    private String ern;
    private Integer intention;
    private Integer likelihood;
    private Boolean cost;
    private Boolean sovereignty;
    private Boolean border;
    private Boolean lift;
    private Boolean hasPV;
    private Boolean wantsPV;
    private Boolean deceased;
    private Boolean poster;
    private String wardCode;

    public static RecordContactRequestBuilder recordContactRequest() {
        return new RecordContactRequestBuilder().withDefaults();
    }

    public RecordContactRequestBuilder withDefaults() {
        withErn("PD-123-1")
                .withIntention(3)
                .withLikelihood(3)
                .withCost(false)
                .withSovereignty(false)
                .withBorder(true)
                .withLift(false)
                .withHasPV(true)
                .withWantsPV(false)
                .withDeceased(false)
                .withPoster(false)
                .withWardCode("E05001221");
        return this;
    }

    public RecordContactRequestBuilder withErn(String ern) {
        this.ern = ern;
        return this;
    }

    public RecordContactRequestBuilder withIntention(Integer intention) {
        this.intention = intention;
        return this;
    }

    public RecordContactRequestBuilder withLikelihood(Integer likelihood) {
        this.likelihood = likelihood;
        return this;
    }

    public RecordContactRequestBuilder withCost(Boolean cost) {
        this.cost = cost;
        return this;
    }

    public RecordContactRequestBuilder withSovereignty(Boolean sovereignty) {
        this.sovereignty = sovereignty;
        return this;
    }

    public RecordContactRequestBuilder withBorder(Boolean border) {
        this.border = border;
        return this;
    }

    public RecordContactRequestBuilder withLift(Boolean lift) {
        this.lift = lift;
        return this;
    }

    public RecordContactRequestBuilder withHasPV(Boolean hasPV) {
        this.hasPV = hasPV;
        return this;
    }

    public RecordContactRequestBuilder withWantsPV(Boolean wantsPV) {
        this.wantsPV = wantsPV;
        return this;
    }

    public RecordContactRequestBuilder withDeceased(Boolean deceased) {
        this.deceased = deceased;
        return this;
    }

    public RecordContactRequestBuilder withPoster(Boolean poster) {
        this.poster = poster;
        return this;
    }

    public RecordContactRequestBuilder withWardCode(String wardCode) {
        this.wardCode = wardCode;
        return this;
    }

    public RecordContactRequest build() {
        return new RecordContactRequest(ern, intention, likelihood, cost, sovereignty, border, lift, hasPV, wantsPV, deceased, poster, wardCode);
    }
}