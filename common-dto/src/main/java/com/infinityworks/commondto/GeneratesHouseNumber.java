package com.infinityworks.commondto;

import com.infinityworks.common.lang.StringExtras;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.joining;

public interface GeneratesHouseNumber {
    String getOrganisationName();

    String getBuildingNumber();

    String getPremise();

    String getSubBuildingName();

    default String getHouseNumber() {
        return newHashSet(getBuildingNumber(), getSubBuildingName(), getPremise(), getOrganisationName())
                .stream()
                .filter(elem -> !StringExtras.isNullOrEmpty(elem))
                .collect(joining(", "));
    }
}
