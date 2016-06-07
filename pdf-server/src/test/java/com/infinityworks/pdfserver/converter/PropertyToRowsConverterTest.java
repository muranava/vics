package com.infinityworks.pdfserver.converter;

import com.infinityworks.pafclient.dto.ImmutableProperty;
import com.infinityworks.pafclient.dto.ImmutableVoting;
import com.infinityworks.pafclient.dto.Property;
import com.infinityworks.pdfserver.pdf.model.ElectorRow;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.pdfserver.testsupport.Fixtures.voterWithDefaults;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PropertyToRowsConverterTest {
    private final PropertyToRowsConverter underTest = new PropertyToRowsConverter();

    @Test
    public void convertsAPropertyToATableRow() throws Exception {
        Property property = household();

        List<ElectorRow> electorRows = underTest.apply("E0123456", property);

        ElectorRow firstVoter = electorRows.get(0);
        assertThat(firstVoter.name(), is("Benz, Andy"));
        assertThat(firstVoter.ern(), is("PD-11-1"));
        assertThat(firstVoter.house(), is("10"));
        assertThat(firstVoter.street(), is("Glen Avenue"));
        assertThat(firstVoter.hasVoted(), is("X"));
        assertThat(firstVoter.likelihood(), is("2"));
        assertThat(firstVoter.support(), is("4"));

        assertThat(electorRows.get(1).name(), is("Benz, Mike"));
        assertThat(electorRows.get(1).ern(), is("PD-22-2"));
        assertThat(electorRows.get(1).house(), is("10"));
        assertThat(electorRows.get(1).street(), is("Glen Avenue"));

        assertThat(electorRows.get(2).name(), is("Benz, Samo"));
        assertThat(electorRows.get(2).ern(), is("PD-33-3"));
        assertThat(electorRows.get(2).house(), is("10"));
        assertThat(electorRows.get(2).street(), is("Glen Avenue"));
    }

    private Property household() {
        return ImmutableProperty.builder()
                .withPostTown("Bournemouth")
                .withPostCode("CV2 3ER")
                .withHouse("10")
                .withStreet("Glen Avenue")
                .withVoters(asList(
                        voterWithDefaults().withFullName("Benz, Andy").withPollingDistrict("PD").withElectorNumber("11").withElectorSuffix("1").withVoting(
                                ImmutableVoting.builder()
                                        .withHasVoted(Boolean.TRUE)
                                        .withIntention(4)
                                        .withLikelihood(2)
                                        .build()
                        ).build(),
                        voterWithDefaults().withFullName("Benz, Mike").withPollingDistrict("PD").withElectorNumber("22").withElectorSuffix("2").build(),
                        voterWithDefaults().withFullName("Benz, Samo").withPollingDistrict("PD").withElectorNumber("33").withElectorSuffix("3").build()
                )).build();
    }
}