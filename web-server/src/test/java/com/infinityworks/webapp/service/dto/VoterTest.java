package com.infinityworks.webapp.service.dto;

import com.google.common.io.Resources;
import com.infinityworks.webapp.paf.dto.Voter;
import org.junit.Test;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VoterTest {
    @Test
    public void deserialisesAProperty() throws Exception {
        Voter voter = objectMapper.readValue(
                Resources.getResource("json/paf-voter.json"), Voter.class);

        assertThat(voter.getFirstName(), is("John"));
        assertThat(voter.getLastName(), is("Deaux"));
        assertThat(voter.getPollingDistrict(), is("AB"));
        assertThat(voter.getElectorNumber(), is("01"));
        assertThat(voter.getElectorSuffix(), is("1"));
        assertThat(voter.getLastName(), is("Deaux"));
        assertThat(voter.getFlags().deceased(), is(false));
        assertThat(voter.getFlags().inaccessible(), is(false));
        assertThat(voter.getFlags().hasPV(), is(false));
        assertThat(voter.getFlags().wantsPV(), is(false));
        assertThat(voter.getIssues().cost(), is(false));
        assertThat(voter.getIssues().control(), is(true));
        assertThat(voter.getIssues().safety(), is(nullValue()));
    }
}
