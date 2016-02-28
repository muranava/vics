package com.infinityworks.webapp.testsupport.builder;

import com.infinityworks.webapp.rest.dto.*;

import static com.infinityworks.webapp.testsupport.builder.StreetBuilder.street;
import static java.util.Collections.singletonList;

public class GenerateGotvCardRequestBuilder {
    private TownStreets townStreets;
    private Integer intentionFrom;
    private Integer intentionTo;
    private Integer likelihoodFrom;
    private Integer likelihoodTo;
    private PostalVote postalVote;

    public GenerateGotvCardRequestBuilder withDefaults() {
        withIntentionFrom(3)
                .withIntentionTo(4)
                .withLikelihoodFrom(3)
                .withLikelihoodTo(4)
                .withPostalVote(PostalVote.WANTS_PV)
                .withTownStreets(new TownStreets(singletonList(
                        street().withMainStreet("Highfield Road").withPostTown("Coventry").build()
                )));
        return this;
    }

    public static GenerateGotvCardRequestBuilder gotvCardRequest() {
        return new GenerateGotvCardRequestBuilder().withDefaults();
    }

    public GenerateGotvCardRequestBuilder withTownStreets(TownStreets townStreets) {
        this.townStreets = townStreets;
        return this;
    }

    public GenerateGotvCardRequestBuilder withIntentionFrom(Integer intentionFrom) {
        this.intentionFrom = intentionFrom;
        return this;
    }

    public GenerateGotvCardRequestBuilder withIntentionTo(Integer intentionTo) {
        this.intentionTo = intentionTo;
        return this;
    }

    public GenerateGotvCardRequestBuilder withLikelihoodFrom(Integer likelihoodFrom) {
        this.likelihoodFrom = likelihoodFrom;
        return this;
    }

    public GenerateGotvCardRequestBuilder withLikelihoodTo(Integer likelihoodTo) {
        this.likelihoodTo = likelihoodTo;
        return this;
    }

    public GenerateGotvCardRequestBuilder withPostalVote(PostalVote postalVote) {
        this.postalVote = postalVote;
        return this;
    }

    public GenerateGotvCardRequest build() {
        return new GenerateGotvCardRequest(townStreets, intentionFrom, intentionTo, likelihoodFrom, likelihoodTo, postalVote);
    }
}
