package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.infinityworks.webapp.domain.Role;
import org.immutables.value.Value;

import java.math.BigInteger;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonSerialize(as = ImmutableUserSummary.class)
public interface UserSummary {
    String id();
    String username();
    String firstName();
    String lastName();
    Role role();
    boolean writeAccess();
    @Value.Default default BigInteger canvassed() {
        return BigInteger.ZERO;
    }
}
