package com.infinityworks.webapp.converter;

import java.util.StringJoiner;

public interface GeneratesStreetLabel {
    String getPostTown();

    String getDependentLocality();

    String getDependentStreet();

    String getBuildingNumber();

    String getMainStreet();

    default String getStreetLabel() {
        return new StringJoiner(", ")
                .add(getDependentStreet())
                .add(getMainStreet())
                .add(getDependentLocality())
                .add(getPostTown())
                .toString();
    }
}
