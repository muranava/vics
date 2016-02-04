package com.infinityworks.webapp.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Temporary table to hold electors with addresses
 */
@Entity
@Table(name = "electors_enriched")
public class ElectorWithAddress extends BaseEntity {

    @Column(nullable = false)
    private String wardCode;

    @Column(nullable = false)
    private String pollingDistrict;

    @Column(nullable = false)
    private String electorId;

    private String electorSuffix;
    private String title;
    private String firstName;
    private String lastName;
    private String flag;
    private String initial;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    private String house;
    private String street;

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getPollingDistrict() {
        return pollingDistrict;
    }

    public void setPollingDistrict(String pollingDistrict) {
        this.pollingDistrict = pollingDistrict;
    }

    public String getElectorId() {
        return electorId;
    }

    public void setElectorId(String electorId) {
        this.electorId = electorId;
    }

    public String getElectorSuffix() {
        return electorSuffix;
    }

    public void setElectorSuffix(String electorSuffix) {
        this.electorSuffix = electorSuffix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getHouse() {
        return house;
    }

    public String getStreet() {
        return street;
    }
}
