package com.infinityworks.commondto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VotersByStreet {
    private final List<Property> properties;

    @JsonCreator
    public VotersByStreet(List<Property> properties) {
        this.properties = MoreObjects.firstNonNull(properties, new ArrayList<>());
    }

    public List<Property> getProperties() {
        return properties;
    }

    public String getMainStreetName() {
        return properties.stream()
                .findFirst()
                .map(Property::getMainStreet)
                .orElse("");
    }
}
