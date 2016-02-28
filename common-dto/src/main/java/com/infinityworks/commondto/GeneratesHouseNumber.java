package com.infinityworks.commondto;

import com.infinityworks.common.lang.StringExtras;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public interface GeneratesHouseNumber {
    String getOrganisationName();

    String getBuildingNumber();

    String getPremise();

    String getSubBuildingName();

    default String getHouseNumber() {
        return newLinkedHashSet(asList(getBuildingNumber(), getSubBuildingName(), getPremise(), getOrganisationName()))
                .stream()
                .filter(elem -> !StringExtras.isNullOrEmpty(elem))
                .collect(joining(", "));
    }
}
