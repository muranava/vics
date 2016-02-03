package com.infinityworks.webapp.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wards")
public class Ward extends BaseEntity {

    private String wardName;

    private String wardCode;

    private String constituencyName;

    private String constituencyCode;

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getConstituencyName() {
        return constituencyName;
    }

    public void setConstituencyName(String constituencyName) {
        this.constituencyName = constituencyName;
    }

    public String getConstituencyCode() {
        return constituencyCode;
    }

    public void setConstituencyCode(String constituencyCode) {
        this.constituencyCode = constituencyCode;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ward)) return false;
        Ward ward = (Ward) o;
        return Objects.equal(wardName, ward.wardName) &&
                Objects.equal(wardCode, ward.wardCode) &&
                Objects.equal(constituencyName, ward.constituencyName) &&
                Objects.equal(constituencyCode, ward.constituencyCode);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(wardName, wardCode, constituencyName, constituencyCode);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wardName", wardName)
                .add("wardCode", wardCode)
                .add("constituencyName", constituencyName)
                .add("constituencyCode", constituencyCode)
                .toString();
    }
}