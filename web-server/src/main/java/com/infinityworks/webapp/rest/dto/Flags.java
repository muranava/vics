package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flags {
    @Min(1)
    @Max(5)
    private final Integer intentionFrom;

    @Min(1)
    @Max(5)
    private final Integer intentionTo;

    @Min(1)
    @Max(5)
    private final Integer likelihoodFrom;

    @Min(1)
    @Max(5)
    private final Integer likelihoodTo;

    private final Boolean hasPV;
    private final Boolean wantsPv;
    private final Boolean needsLift;
    private final Boolean notCanvassedYet;
    private final Boolean poster;

    @JsonCreator
    public Flags(@JsonProperty("intentionFrom") Integer intentionFrom,
                 @JsonProperty("intentionTo") Integer intentionTo,
                 @JsonProperty("likelihoodFrom") Integer likelihoodFrom,
                 @JsonProperty("likelihoodTo") Integer likelihoodTo,
                 @JsonProperty("hasPV") Boolean hasPV,
                 @JsonProperty("wantsPV") Boolean wantsPv,
                 @JsonProperty("lift") Boolean needsLift,
                 @JsonProperty("canvassed") Boolean notCanvassedYet,
                 @JsonProperty("poster") Boolean poster) {
        this.intentionFrom = intentionFrom;
        this.intentionTo = intentionTo;
        this.likelihoodFrom = likelihoodFrom;
        this.likelihoodTo = likelihoodTo;
        this.hasPV = hasPV;
        this.wantsPv = wantsPv;
        this.needsLift = needsLift;
        this.notCanvassedYet = notCanvassedYet;
        this.poster = poster;
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

    public Boolean getHasPV() {
        return hasPV;
    }

    public Boolean getWantsPv() {
        return wantsPv;
    }

    public Boolean getNeedsLift() {
        return needsLift;
    }

    public Boolean getNotCanvassedYet() {
        return notCanvassedYet;
    }

    public Boolean getPoster() {
        return poster;
    }
}
