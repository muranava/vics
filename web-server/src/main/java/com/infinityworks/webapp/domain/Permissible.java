package com.infinityworks.webapp.domain;

public interface Permissible {
    boolean hasWardPermission(Ward ward);

    boolean hasConstituencyPermission(Constituency constituency);

    boolean isAdmin();
}
