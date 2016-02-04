package com.infinityworks.webapp.converter;

import static com.infinityworks.webapp.common.lang.StringExtras.isNullOrEmpty;

public interface GeneratesErn {

    String getElectorId();

    String getWardCode();

    String getElectorSuffix();

    default String getErn() {
        String mandatoryPart = getWardCode() + "/" + getElectorId();
        return isNullOrEmpty(getElectorSuffix())
                ? mandatoryPart
                : mandatoryPart + "/" + getElectorSuffix();
    }
}
