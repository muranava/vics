package com.infinityworks.webapp.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ErnTest {
    @Test
    public void shouldExtractTheErnParts() throws Exception {
        Ern ern = Ern.valueOf("E0900123-T-123-4");

        assertThat(ern.get(), is("E0900123-T-123-4"));
    }
}