package com.infinityworks.commondto;

import com.infinityworks.common.lang.StringExtras;

public interface GeneratesErn {

    String getPollingDistrict();

    String getElectorId();

    String getElectorSuffix();

    default String getErn() {
        String mandatoryPart = getPollingDistrict() + "/" + getElectorId();
        return StringExtras.isNullOrEmpty(getElectorSuffix())
                ? mandatoryPart
                : mandatoryPart + "/" + getElectorSuffix();
    }
}
