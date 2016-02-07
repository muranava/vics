package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class AuthenticationToken {
    private final String username;
    private final Collection<String> roles;
    private final String token;

    @JsonCreator
    public AuthenticationToken(@JsonProperty("username") String username,
                               @JsonProperty("roles") Collection<String> roles,
                               @JsonProperty("token") String token) {
        this.roles = roles;
        this.token = token;
        this.username = username;
    }

    public Collection<String> getRoles() {
        return this.roles;
    }

    public String getToken() {
        return this.token;
    }

    public String getUsername() {
        return username;
    }
}
