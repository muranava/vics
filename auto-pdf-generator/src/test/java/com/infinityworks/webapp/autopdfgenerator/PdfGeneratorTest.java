package com.infinityworks.webapp.autopdfgenerator;

import com.google.common.collect.Iterables;
import org.junit.Test;

import static java.util.Arrays.asList;

public class PdfGeneratorTest {
    @Test
    public void batchProcessDiscovery() throws Exception {

        Iterables.partition(asList(1, 2, 3, 4, 5, 6), 2)
                .forEach(e -> {

                });

    }
}