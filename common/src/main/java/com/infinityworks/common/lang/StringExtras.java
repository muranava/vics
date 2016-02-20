package com.infinityworks.common.lang;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class StringExtras {

    public static boolean isNullOrEmpty(String underTest) {
        return underTest == null || underTest.isEmpty();
    }

    public static List<String> toUpperCase(List<String> toTx) {
        return toTx.stream()
                .map(String::toUpperCase)
                .collect(toList());
    }

    private StringExtras() {
        throw new UnsupportedOperationException("Do not instantiate");
    }
}
