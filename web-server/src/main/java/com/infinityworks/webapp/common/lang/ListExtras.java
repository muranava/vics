package com.infinityworks.webapp.common.lang;

import java.util.List;

/**
 * Additions to lists
 */
public final class ListExtras {

    public static boolean isNullOrEmpty(List<?> underTest) {
        return underTest == null || underTest.isEmpty();
    }

    public static boolean noneNull(List<?> underTest) {
        return underTest != null && underTest.stream().allMatch(elem -> elem != null);
    }

    private ListExtras() {
        throw new UnsupportedOperationException("Do not instantiate");
    }
}
