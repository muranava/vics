package com.infinityworks.webapp.testsupport.builder;

import com.infinityworks.webapp.domain.Ward;

public class WardBuilder {
    private String wardName;
    private String wardCode;
    private String constituencyName;
    private String constituencyCode;

    public static WardBuilder ward() {
        return new WardBuilder().withDefaults();
    }

    public WardBuilder withDefaults() {
        withWardName("Binley")
                .withWardCode("COV-BNLY-1")
                .withConstituencyCode("COV-1987")
                .withConstituencyName("Coventry South");
        return this;
    }

    public WardBuilder withWardName(String val) {
        wardName = val;
        return this;
    }

    public WardBuilder withWardCode(String val) {
        wardCode = val;
        return this;
    }

    public WardBuilder withConstituencyName(String val) {
        constituencyName = val;
        return this;
    }

    public WardBuilder withConstituencyCode(String val) {
        constituencyCode = val;
        return this;
    }

    public Ward build() {
        Ward ward = new Ward();
        ward.setWardName(wardName);
        ward.setWardCode(wardCode);
//        ward.setConstituencyName(constituencyName);
//        ward.setConstituencyCode(constituencyCode);
        return ward;
    }

    private WardBuilder() {
    }
}
