package com.infinityworks.commondto;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The voter ID that underpins all interactions in the system regarding a voter
 *
 * {wardCode}-{pollingDistrict}-{electorId}-{electorSuffix}
 *
 * where electorSuffix is optional and will not include the leading hyphen if absent.
 */
public class Ern {
    private final String wardCode;
    private final String pollingDistrict;
    private final String electorID;
    private final String electorSuffix;
    private static final String ERN_REGEX_PATTERN =
            Pattern.compile("\\w+-\\w+-\\w+(-\\w+)?").pattern();

    public Ern(String wardCode,
               String pollingDistrict,
               String electorID,
               String electorSuffix) {
        this.wardCode = wardCode;
        this.pollingDistrict = pollingDistrict;
        this.electorID = electorID;
        this.electorSuffix = electorSuffix;
    }

    public static Ern valueOf(String ern) {
        if (ern.matches(ERN_REGEX_PATTERN)) {
            return parseErn(ern);
        } else {
            throw new IllegalArgumentException(String.format("Invalid ERN format. Received %s", ern));
        }
    }

    private static Ern parseErn(String ern) {
        String[] parts = ern.split("-");
        if (parts.length == 4) {
            return new Ern(parts[0], parts[1], parts[2], parts[3]);
        } else {
            return new Ern(parts[0], parts[1], parts[2], null);
        }
    }

    public String getWardCode() {
        return wardCode;
    }

    public String getPollingDistrict() {
        return pollingDistrict;
    }

    public String getElectorID() {
        return electorID;
    }

    public Optional<String> getElectorSuffix() {
        return Optional.ofNullable(electorSuffix);
    }
}
