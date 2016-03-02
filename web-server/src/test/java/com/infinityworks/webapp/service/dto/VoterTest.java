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

        assertThat(voter.firstName(), is("John"));
        assertThat(voter.lastName(), is("Deaux"));
        assertThat(voter.pollingDistrict(), is("AB"));
        assertThat(voter.electorNumber(), is("01"));
        assertThat(voter.electorSuffix(), is("1"));
        assertThat(voter.lastName(), is("Deaux"));
        assertThat(voter.flags().deceased(), is(false));
        assertThat(voter.flags().inaccessible(), is(false));
        assertThat(voter.flags().hasPV(), is(false));
        assertThat(voter.flags().wantsPV(), is(false));
        assertThat(voter.issues().cost(), is(false));
        assertThat(voter.issues().sovereignty(), is(true));
        assertThat(voter.issues().border(), is(nullValue()));
    }
}
