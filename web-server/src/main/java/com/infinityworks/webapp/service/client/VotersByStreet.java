package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VotersByStreet {
    private final List<Property> properties;

    @JsonCreator
    public VotersByStreet(List<Property> properties) {
        this.properties = properties;
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
