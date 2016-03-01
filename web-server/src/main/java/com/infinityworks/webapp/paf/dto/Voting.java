package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Voting {
    private final Integer intention;
    private final Integer likelihood;

    @JsonCreator
    public Voting(@JsonProperty("intention") Integer intention,
                  @JsonProperty("property") Integer likelihood) {
        this.intention = intention;
        this.likelihood = likelihood;
    }

    public Integer getIntention() {
        return intention;
    }

    public Integer getLikelihood() {
        return likelihood;
    }
}
