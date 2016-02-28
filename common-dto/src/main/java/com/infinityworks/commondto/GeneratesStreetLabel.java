package com.infinityworks.commondto;

import java.util.StringJoiner;

public interface GeneratesStreetLabel {
    String getPostTown();

    String getPostCode();

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
                .add(getPostCode())
                .toString();
    }
}
