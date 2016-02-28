package com.infinityworks.commondto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.nullToEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Property implements GeneratesStreetLabel, GeneratesHouseNumber {
    private final String buildingNumber;
    private final String subBuildingNumber;
    private final String mainStreet;
    private final String postTown;
    private final String dependentLocality;
    private final String dependentStreet;
    private final String postCode;
    private final String premise;
    private final String departmentName;
    private final String organisationName;

    private final List<Voter> voters;

    @JsonCreator
    public Property(@JsonProperty("building_number") String buildingNumber,
                    @JsonProperty("sub_building_name") String subBuildingNumber,
                    @JsonProperty("main_street") String mainStreet,
                    @JsonProperty("post_town") String postTown,
                    @JsonProperty("dependent_locality") String dependentLocality,
                    @JsonProperty("dependent_street") String dependentStreet,
                    @JsonProperty("voters") List<Voter> voters,
                    @JsonProperty("postcode") String postCode,
                    @JsonProperty("premise") String premise,
                    @JsonProperty("department_name") String departmentName,
                    @JsonProperty("organisation_name") String organisationName) {
        this.postCode = postCode;
        this.subBuildingNumber = subBuildingNumber;
        this.premise = premise;
        this.departmentName = departmentName;
        this.organisationName = organisationName;
        this.buildingNumber = nullToEmpty(buildingNumber);
        this.mainStreet = nullToEmpty(mainStreet);
        this.postTown = nullToEmpty(postTown);
        this.dependentLocality = nullToEmpty(dependentLocality);
        this.dependentStreet = nullToEmpty(dependentStreet);
        this.voters = firstNonNull(voters, new ArrayList<>());
    }

    @Override
    public String getPostTown() {
        return postTown;
    }

    @Override
    public String getDependentLocality() {
        return dependentLocality;
    }

    @Override
    public String getDependentStreet() {
        return dependentStreet;
    }

    @Override
    public String getBuildingNumber() {
        return buildingNumber;
    }

    @Override
    public String getMainStreet() {
        return mainStreet;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    @Override
    public String getPostCode() {
        return postCode;
    }

    @Override
    public String getPremise() {
        return premise;
    }

    @Override
    public String getSubBuildingName() {
        return subBuildingNumber;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String getOrganisationName() {
        return organisationName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("voters", voters)
                .add("organisationName", organisationName)
                .add("departmentName", departmentName)
                .add("premise", premise)
                .add("postCode", postCode)
                .add("dependentStreet", dependentStreet)
                .add("dependentLocality", dependentLocality)
                .add("postTown", postTown)
                .add("mainStreet", mainStreet)
                .add("buildingNumber", buildingNumber)
                .add("subBuildingNumber", subBuildingNumber)
                .toString();
    }
}
