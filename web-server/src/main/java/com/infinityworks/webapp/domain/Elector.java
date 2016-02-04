package com.infinityworks.webapp.domain;

import com.infinityworks.webapp.converter.GeneratesErn;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "electors")
public class Elector extends BaseEntity implements GeneratesErn {

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

    public Elector() {
        // required by hibernate
    }

    public Elector(String wardCode, String pollingDistrict, String electorId, String electorSuffix, String title, String firstName, String lastName, String flag, String initial, Date dob, Date modified, Date created) {
        this.wardCode = wardCode;
        this.pollingDistrict = pollingDistrict;
        this.electorId = electorId;
        this.electorSuffix = electorSuffix;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.flag = flag;
        this.initial = initial;
        this.dob = dob;
        this.modified = modified;
        this.created = created;
    }

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
