package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flags {
    private final Boolean hasPV;
    private final Boolean wantsPV;
    private final Boolean lift;
    private final Boolean deceased;
    private final Boolean inaccessible;

    @JsonCreator
    public Flags(@JsonProperty("has_postal") Boolean hasPV,
                 @JsonProperty("wants_postal") Boolean wantsPV,
                 @JsonProperty("lift") Boolean lift,
                 @JsonProperty("deceased") Boolean deceased,
                 @JsonProperty("inaccesible") Boolean inaccessible) {
        this.hasPV = hasPV;
        this.wantsPV = wantsPV;
        this.lift = lift;
        this.deceased = deceased;
        this.inaccessible = inaccessible;
    }

    public Boolean getHasPV() {
        return hasPV;
    }

    public Boolean getWantsPV() {
        return wantsPV;
    }

    public Boolean getLift() {
        return lift;
    }

    public Boolean getDeceased() {
        return deceased;
    }

    public Boolean getInaccessible() {
        return inaccessible;
    }
}
