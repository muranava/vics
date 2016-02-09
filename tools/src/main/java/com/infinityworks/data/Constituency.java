package com.infinityworks.data;

import com.infinityworks.webapp.domain.BaseEntity;

import java.util.UUID;

public class Constituency extends BaseEntity {
    private UUID id;
    private String code;
    private String name;

    public Constituency(String code, String name) {
        id = UUID.randomUUID();
        this.code = code;
        this.name = name;
    }

    @Override
    public UUID getId() {
        return id;
    }

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

