package com.infinityworks.webapp.domain;

import com.google.common.base.Objects;

import javax.persistence.*;

@Entity
@Table(name = "wards")
public class Ward extends BaseEntity {
    private String code;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Constituency constituency;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConstituency(Constituency constituency) {
        this.constituency = constituency;
    }

    public Constituency getConstituency() {
        return constituency;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ward)) return false;
        Ward ward = (Ward) o;
        return Objects.equal(code, ward.code) &&
                Objects.equal(name, ward.name) &&
                Objects.equal(constituency.getCode(), ward.constituency.getCode());
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(code, name, constituency.getCode());
    }
}
