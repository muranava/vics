package com.infinityworks.webapp.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PafRecord {

    private final Integer udprn;
    private final String departmentName;
    private final String organisationName;
    private final String subBuildingName;
    private final String buildingName;
    private final String poBoxNumber;
    private final String buildingNumber;
    private final String dependentStreet;
    private final String mainStreet;
    private final String doubleDependentLocality;
    private final String postTown;
    private final String postcode;
    private final List<Voter> voters;

    @JsonCreator
    public PafRecord(@JsonProperty("udprn") Integer udprn,
                     @JsonProperty("department_name") String departmentName,
                     @JsonProperty("organisationName") String organisationName,
                     @JsonProperty("subBuildingName") String subBuildingName,
                     @JsonProperty("buildingName") String buildingName,
                     @JsonProperty("poBoxNumber") String poBoxNumber,
                     @JsonProperty("buildingNumber") String buildingNumber,
                     @JsonProperty("dependentStreet") String dependentStreet,
                     @JsonProperty("mainStreet") String mainStreet,
                     @JsonProperty("doubleDependentLocality") String doubleDependentLocality,
                     @JsonProperty("postTown") String postTown,
                     @JsonProperty("postcode") String postcode,
                     @JsonProperty("voters") List<Voter> voters) {

        this.udprn = udprn;
        this.departmentName = departmentName;
        this.organisationName = organisationName;
        this.subBuildingName = subBuildingName;
        this.buildingName = buildingName;
        this.poBoxNumber = poBoxNumber;
        this.buildingNumber = buildingNumber;
        this.dependentStreet = dependentStreet;
        this.mainStreet = mainStreet;
        this.doubleDependentLocality = doubleDependentLocality;
        this.postTown = postTown;
        this.postcode = postcode;
        this.voters = voters;
    }

    public Integer getUdprn() {
        return udprn;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getSubBuildingName() {
        return subBuildingName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getPoBoxNumber() {
        return poBoxNumber;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public String getDependentStreet() {
        return dependentStreet;
    }

    public String getMainStreet() {
        return mainStreet;
    }

    public String getDoubleDependentLocality() {
        return doubleDependentLocality;
    }

    public String getPostTown() {
        return postTown;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<Voter> getVoters() {
        return voters;
    }
}
