package com.infinityworks.commondto;

import com.infinityworks.common.lang.StringExtras;

public interface GeneratesHouseNumber {
    String getOrganisationName();

    String getBuildingNumber();

    String getPremise();

    String getSubBuildingName();

    default String getHouseNumber() {
        if (!StringExtras.isNullOrEmpty(getSubBuildingName())) {
            return String.format("%s \n%s", getBuildingNumber(), getSubBuildingName());
        } else if(!StringExtras.isNullOrEmpty(getBuildingNumber())) {
            return getBuildingNumber();
        } else if (!StringExtras.isNullOrEmpty(getOrganisationName())) {
            return getOrganisationName();
        } else if (!StringExtras.isNullOrEmpty(getPremise())) {
            return getPremise();
        } else {
            return "-";
        }
    }
}
