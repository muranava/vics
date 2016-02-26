package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GenerateGotvCardRequest {
    @Valid
    @NotNull
    private final TownStreets townStreets;

    @NotNull
    @Min(1)
    @Max(5)
    private final Integer intentionFrom;

    @NotNull
    @Min(1)
    @Max(5)
    private final Integer intentionTo;

    @NotNull
    @Min(1)
    @Max(5)
    private final Integer likelihoodFrom;

    @NotNull
    @Min(1)
    @Max(5)
    private final Integer likelihoodTo;

    @NotNull
    private final PostalVote postalVote;

    @JsonCreator
    public GenerateGotvCardRequest(@JsonProperty("townStreets") TownStreets townStreets,
                                   @JsonProperty("intentionFrom") Integer intentionFrom,
                                   @JsonProperty("intentionTo") Integer intentionTo,
                                   @JsonProperty("likelihoodFrom") Integer likelihoodFrom,
                                   @JsonProperty("likelihoodTo") Integer likelihoodTo,
                                   @JsonProperty("postalVote") PostalVote postalVote) {
        this.townStreets = townStreets;
        this.intentionFrom = intentionFrom;
        this.intentionTo = intentionTo;
        this.likelihoodFrom = likelihoodFrom;
        this.likelihoodTo = likelihoodTo;
        this.postalVote = postalVote;
    }

    public TownStreets getTownStreets() {
        return townStreets;
    }

    public Integer getIntentionFrom() {
        return intentionFrom;
    }

    public Integer getIntentionTo() {
        return intentionTo;
    }

    public Integer getLikelihoodFrom() {
        return likelihoodFrom;
    }

    public Integer getLikelihoodTo() {
        return likelihoodTo;
    }

    public PostalVote getPostalVote() {
        return postalVote;
    }
}
