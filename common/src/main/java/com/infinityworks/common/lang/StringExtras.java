package com.infinityworks.common.lang;

public final class StringExtras {

    public static boolean isNullOrEmpty(String underTest) {
        return underTest == null || underTest.isEmpty();
    }

    private StringExtras() {
        throw new UnsupportedOperationException("Do not instantiate");
    }
}
