package com.infinityworks.testsupport.builder;

import com.infinityworks.commondto.Property;
import com.infinityworks.commondto.Voter;

import java.util.Collections;
import java.util.List;

public class PropertyBuilder {
    private String buildingNumber;
    private String subBuildingNumber;
    private String mainStreet;
    private String postTown;
    private String dependentLocality;
    private String dependentStreet;
    private List<Voter> voters;
    private String organisationName;
    private String departmentName;
    private String premise;
    private String postCode;

    public static PropertyBuilder property() {
        return new PropertyBuilder().withDefaults();
    }

    public PropertyBuilder withDefaults() {
        withBuildingNumber("12")
                .withDependentLocality("")
                .withDependentStreet("Highfield Road")
                .withMainStreet("Mortimer Crescent")
                .withPostTown("Coventry")
                .withPremise("")
                .withDepartmentName("")
                .withPostCode("CV2 3ER")
                .withOrganisationName("")
                .withSubBuildingNumber("")
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

    public PropertyBuilder withPostCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public PropertyBuilder withPremise(String premise) {
        this.premise = premise;
        return this;
    }

    public PropertyBuilder withDepartmentName(String dept) {
        this.departmentName = dept;
        return this;
    }

    public PropertyBuilder withOrganisationName(String orgName) {
        this.organisationName = orgName;
        return this;
    }

    public PropertyBuilder withSubBuildingNumber(String sbNumber) {
        this.subBuildingNumber = sbNumber;
        return this;
    }

    public Property build() {
        return new Property(buildingNumber, subBuildingNumber, mainStreet, postTown, dependentLocality, dependentStreet, voters, postCode, premise, departmentName, organisationName);
    }
}