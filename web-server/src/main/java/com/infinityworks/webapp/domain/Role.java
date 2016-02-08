package com.infinityworks.webapp.domain;

import com.infinityworks.webapp.common.Try;

import javax.validation.ValidationException;

public enum Role {
    USER, ADMIN;

    public static Try<Role> of(String role) {
        try {
            return Try.success(Role.valueOf(role));
        } catch (IllegalArgumentException e) {
            return Try.failure(new ValidationException("Invalid role"));
        }
    }

    public static boolean hasPermission(Role underTest, Role requiredUser) {
        return !(requiredUser == Role.ADMIN && underTest == Role.USER);
    }
}
