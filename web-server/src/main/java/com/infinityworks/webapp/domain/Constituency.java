package com.infinityworks.webapp.domain;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "constituencies")
public class Constituency extends BaseEntity {
    private String code;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Constituency)) return false;
        Constituency that = (Constituency) o;
        return Objects.equal(code, that.code) &&
                Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code, name);
    }
}
