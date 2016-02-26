package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GenerateGotvCardRequest {
    @Valid @NotNull
    private final TownStreets townStreets;

    @NotNull @Min(1) @Max(5)
    private final Integer intention;

    @NotNull @Min(1) @Max(5)
    private final Integer likelihood;

    @NotNull
    private final PostalVote postalVote;

    @JsonCreator
    public GenerateGotvCardRequest(@JsonProperty("townStreets") TownStreets townStreets,
                                   @JsonProperty("intention") Integer intention,
                                   @JsonProperty("likelihood") Integer likelihood,
                                   @JsonProperty("postalVote") PostalVote postalVote) {
        this.townStreets = townStreets;
        this.intention = intention;
        this.likelihood = likelihood;
        this.postalVote = postalVote;
    }

    public TownStreets getTownStreets() {
        return townStreets;
    }

    public Integer getIntention() {
        return intention;
    }

    public Integer getLikelihood() {
        return likelihood;
    }

    public PostalVote getPostalVote() {
        return postalVote;
    }
}
