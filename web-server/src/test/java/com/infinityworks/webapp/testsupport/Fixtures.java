package com.infinityworks.webapp.testsupport;

import com.infinityworks.webapp.rest.dto.Street;

public class Fixtures {

    public static Street kirbyRoad() {
        return new Street("Kirby Road", "Coventry", "Northern Quarter", "");
    }

    public static Street abbotRoad() {
        return new Street("Abbot Road", "Coventry", "Southern Quarter", "");
    }
}
