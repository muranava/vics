package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TownStreets {
    @NotEmpty
    @NotNull
    private final List<Street> streets;

    @JsonCreator
    public TownStreets(@JsonProperty("streets") List<Street> streets) {
        this.streets = streets;
    }

    public List<Street> getStreets() {
        return streets;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("streets", streets)
                .toString();
    }
}
