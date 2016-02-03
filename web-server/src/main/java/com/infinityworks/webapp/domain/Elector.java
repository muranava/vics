package com.infinityworks.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "electors")
public class Elector extends BaseEntity {

    private String wardCode;
    private String pollingDistrict;
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

    // temporary fields so we can print cards in the short-term
    // these fields will go into the

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
}
