package com.infinityworks.webapp.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public final class ConstituencyName {
    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    public ConstituencyName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ConstituencyName of(String name) {
        return new ConstituencyName(name);
    }
}
