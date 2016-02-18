package com.infinityworks.webapp.converter;

import com.infinityworks.pdfgen.ElectorRow;
import com.infinityworks.webapp.service.client.Property;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.PropertyBuilder.property;
import static com.infinityworks.webapp.testsupport.builder.VoterBuilder.voter;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PropertyToRowConverterTest {
    private final PropertyToRowConverter underTest = new PropertyToRowConverter();

    @Test
    public void convertsAPropertyToATableRow() throws Exception {
        Property property = property()
                .withBuildingNumber("10")
                .withDependentLocality("Amber")
                .withDependentStreet("Highland Close")
                .withPostTown("Bournemouth")
                .withMainStreet("Glen Avenue")
                .withVoters(asList(
                        voter().withFirstName("Andy").withElectorId("1").withElectorSuffix("11").withTelephone("0987654321").withLastName("Benz").build(),
                        voter().withFirstName("Mike").withElectorId("2").withElectorSuffix("22").withTelephone("0987654321").withLastName("Benz").build(),
                        voter().withFirstName("Samo").withElectorId("3").withElectorSuffix("33").withTelephone("0987654321").withLastName("Benz").build()
                )).build();

        List<ElectorRow> electorRows = underTest.apply("E0123456", property);

        assertThat(electorRows.get(0).getName(), is("Benz, Andy"));
        assertThat(electorRows.get(0).getErn(), is("E0123456/1/11"));
        assertThat(electorRows.get(0).getHouse(), is("10"));
        assertThat(electorRows.get(0).getStreet(), is("Highland Close, Glen Avenue, Amber, Bournemouth"));

        assertThat(electorRows.get(1).getName(), is("Benz, Mike"));
        assertThat(electorRows.get(1).getErn(), is("E0123456/2/22"));
        assertThat(electorRows.get(1).getHouse(), is("10"));
        assertThat(electorRows.get(1).getStreet(), is("Highland Close, Glen Avenue, Amber, Bournemouth"));

        assertThat(electorRows.get(2).getName(), is("Benz, Samo"));
        assertThat(electorRows.get(2).getErn(), is("E0123456/3/33"));
        assertThat(electorRows.get(2).getHouse(), is("10"));
        assertThat(electorRows.get(2).getStreet(), is("Highland Close, Glen Avenue, Amber, Bournemouth"));

    }
}