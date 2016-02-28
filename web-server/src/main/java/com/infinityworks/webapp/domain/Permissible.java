package com.infinityworks.webapp.domain;

import java.util.Set;

public interface Permissible {
    default boolean hasWardPermission(Ward ward) {
        return getWards().contains(ward) ||
               getConstituencies().contains(ward.getConstituency());
    }

    default boolean hasConstituencyPermission(Constituency constituency) {
        return getConstituencies().contains(constituency);
    }

    default boolean hasPermission(Permission permission) {
        return getPermissions().stream().anyMatch(privilege -> privilege.getPermission() == permission);
    }

    default Boolean hasWriteAccess() {
        return getWriteAccess();
    }

    default boolean isAdmin() {
        return getRole() == Role.ADMIN;
    }

    Boolean getWriteAccess();

    Set<Ward> getWards();

    Set<Constituency> getConstituencies();

    Set<Privilege> getPermissions();

    Role getRole();
}
