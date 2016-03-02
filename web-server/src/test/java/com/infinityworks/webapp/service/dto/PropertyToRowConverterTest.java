package com.infinityworks.webapp.service.dto;

import com.infinityworks.pdfgen.model.ElectorRow;
import com.infinityworks.webapp.converter.PropertyToRowConverter;
import com.infinityworks.webapp.paf.dto.ImmutableProperty;
import com.infinityworks.webapp.paf.dto.Property;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.webapp.testsupport.Fixtures.voterWithDefaults;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PropertyToRowConverterTest {
    private final PropertyToRowConverter underTest = new PropertyToRowConverter();

    @Test
    public void convertsAPropertyToATableRow() throws Exception {
        Property property = ImmutableProperty.builder()
                .withPostTown("Bournemouth")
                .withPostCode("CV2 3ER")
                .withHouse("10")
                .withStreet("Glen Avenue")
                .withVoters(asList(
                        voterWithDefaults().withFirstName("Andy").withPollingDistrict("PD").withElectorNumber("11").withElectorSuffix("1").withTelephone("0987654321").withLastName("Benz").build(),
                        voterWithDefaults().withFirstName("Mike").withPollingDistrict("PD").withElectorNumber("22").withElectorSuffix("2").withTelephone("0987654321").withLastName("Benz").build(),
                        voterWithDefaults().withFirstName("Samo").withPollingDistrict("PD").withElectorNumber("33").withElectorSuffix("3").withTelephone("0987654321").withLastName("Benz").build()
                )).build();

        List<ElectorRow> electorRows = underTest.apply("E0123456", property);

        assertThat(electorRows.get(0).getName(), is("Benz, Andy"));
        assertThat(electorRows.get(0).getErn(), is("PD-11-1"));
        assertThat(electorRows.get(0).getHouse(), is("10"));
        assertThat(electorRows.get(0).getStreet(), is("Glen Avenue"));

        assertThat(electorRows.get(1).getName(), is("Benz, Mike"));
        assertThat(electorRows.get(1).getErn(), is("PD-22-2"));
        assertThat(electorRows.get(1).getHouse(), is("10"));
        assertThat(electorRows.get(1).getStreet(), is("Glen Avenue"));

        assertThat(electorRows.get(2).getName(), is("Benz, Samo"));
        assertThat(electorRows.get(2).getErn(), is("PD-33-3"));
        assertThat(electorRows.get(2).getHouse(), is("10"));
        assertThat(electorRows.get(2).getStreet(), is("Glen Avenue"));
    }
}