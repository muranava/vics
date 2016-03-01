package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issues {
    private final Boolean cost;
    private final Boolean control;
    private final Boolean safety;

    public Issues(@JsonProperty("cost") Boolean cost,
                  @JsonProperty("control") Boolean control,
                  @JsonProperty("safety") Boolean safety) {
        this.cost = cost;
        this.control = control;
        this.safety = safety;
    }

    public Boolean getCost() {
        return cost;
    }

    public Boolean getControl() {
        return control;
    }

    public Boolean getSafety() {
        return safety;
    }
}
