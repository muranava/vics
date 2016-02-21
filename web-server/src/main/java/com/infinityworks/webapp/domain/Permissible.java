package com.infinityworks.webapp.domain;

import java.util.Set;

public interface Permissible {
    default boolean hasWardPermission(Ward ward) {
        return getWards().contains(ward);
    }

    default boolean hasConstituencyPermission(Constituency constituency) {
        return getConstituencies().contains(constituency);
    }

    default boolean hasPermission(Permission permission) {
        return getPermissions().stream().anyMatch(privilege -> privilege.getPermission() == permission);
    }

    default boolean isAdmin() {
        return getRole() == Role.ADMIN;
    }

    Set<Ward> getWards();

    Set<Constituency> getConstituencies();

    Set<Privilege> getPermissions();

    Role getRole();
}
