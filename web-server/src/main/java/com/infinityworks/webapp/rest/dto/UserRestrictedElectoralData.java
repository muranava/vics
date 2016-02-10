package com.infinityworks.webapp.rest.dto;

import com.infinityworks.webapp.domain.Ward;

import java.util.Set;

public class UserRestrictedElectoralData {
    private Set<Ward> wards;

    public UserRestrictedElectoralData(Set<Ward> wards) {
        this.wards = wards;
    }

    public Set<Ward> getWards() {
        return wards;
    }
}
