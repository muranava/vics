package com.infinityworks.webapp.paf.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.infinityworks.common.lang.StringExtras;

import java.util.ArrayList;
import java.util.List;

import static com.infinityworks.common.lang.StringExtras.isNullOrEmpty;

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

    public String getStreetLabel() {
        return properties.stream()
                .findFirst()
                .map(property -> {
                    String street = property.getStreet();
                    String postCode = property.getPostCode();
                    return isNullOrEmpty(street)
                            ? postCode
                            : street + ", " + postCode;
                })
                .orElse("");
    }
}
