package com.infinityworks.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "electoral_wards")
public class ElectoralWard {
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
}
