package com.infinityworks.commondto;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ErnTest {
    @Test
    public void parsesTheErnIntoPartsWithSuffix() throws Exception {
        Ern ern = Ern.valueOf("E0988414-ABA-13041-1");

        assertThat(ern.getWardCode(), is("E0988414"));
        assertThat(ern.getPollingDistrict(), is("ABA"));
        assertThat(ern.getElectorID(), is("13041"));
        assertThat(ern.getElectorSuffix(), is(Optional.of("1")));
    }

    @Test
    public void parsesTheErnIntoPartsWithoutSuffix() throws Exception {
        Ern ern = Ern.valueOf("E0988414-ABA-13041");

        assertThat(ern.getWardCode(), is("E0988414"));
        assertThat(ern.getPollingDistrict(), is("ABA"));
        assertThat(ern.getElectorID(), is("13041"));
        assertThat(ern.getElectorSuffix(), is(Optional.empty()));
    }
}
