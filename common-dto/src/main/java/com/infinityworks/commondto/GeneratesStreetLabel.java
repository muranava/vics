package com.infinityworks.commondto;

import com.infinityworks.common.lang.StringExtras;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public interface GeneratesStreetLabel {
    String getPostCode();

    String getDependentLocality();

    String getDependentStreet();

    String getMainStreet();

    default String getStreetLabel() {
        return newLinkedHashSet(asList(getDependentStreet(), getMainStreet(), getDependentLocality(), getPostCode()))
                .stream()
                .filter(elem -> !StringExtras.isNullOrEmpty(elem))
                .collect(joining(", "));
    }
}
