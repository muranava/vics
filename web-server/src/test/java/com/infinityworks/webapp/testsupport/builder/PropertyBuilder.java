package com.infinityworks.webapp.testsupport.builder;

import com.infinityworks.webapp.service.client.Property;
import com.infinityworks.webapp.service.client.Voter;

import java.util.Collections;
import java.util.List;

public class PropertyBuilder {
    private String buildingNumber;
    private String mainStreet;
    private String postTown;
    private String dependentLocality;
    private String dependentStreet;
    private List<Voter> voters;

    public static PropertyBuilder property() {
        return new PropertyBuilder().withDefaults();
    }

    public PropertyBuilder withDefaults() {
        withBuildingNumber("12")
                .withDependentLocality("")
                .withDependentStreet("Highfield Road")
                .withMainStreet("Mortimer Crescent")
                .withPostTown("Coventry")
                .withVoters(Collections.emptyList());
        return this;
    }

    public PropertyBuilder withBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
        return this;
    }

    public PropertyBuilder withMainStreet(String mainStreet) {
        this.mainStreet = mainStreet;
        return this;
    }

    public PropertyBuilder withPostTown(String postTown) {
        this.postTown = postTown;
        return this;
    }

    public PropertyBuilder withDependentLocality(String dependentLocality) {
        this.dependentLocality = dependentLocality;
        return this;
    }

    public PropertyBuilder withDependentStreet(String dependentStreet) {
        this.dependentStreet = dependentStreet;
        return this;
    }

    public PropertyBuilder withVoters(List<Voter> voters) {
        this.voters = voters;
        return this;
    }

    public Property build() {
        return new Property(buildingNumber, mainStreet, postTown, dependentLocality, dependentStreet, voters);
    }
}