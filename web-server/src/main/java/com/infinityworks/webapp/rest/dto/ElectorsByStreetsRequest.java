package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinityworks.commondto.Flags;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElectorsByStreetsRequest {
    @Valid
    @NotNull
    @Size(min = 1)
    private final List<Street> streets;

    // optional (used for gotv requests)
    @Valid
    private final Flags flags;

    @JsonCreator
    public ElectorsByStreetsRequest(@JsonProperty("streets") List<Street> streets,
                                    @JsonProperty("flags") Flags flags) {
        this.streets = streets;
        this.flags = flags;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public Flags getFlags() {
        return flags;
    }
}
