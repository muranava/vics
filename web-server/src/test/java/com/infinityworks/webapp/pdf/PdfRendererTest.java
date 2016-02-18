package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.GeneratedPdfTable;
import com.infinityworks.pdfgen.TableBuilder;
import com.infinityworks.webapp.converter.PropertyToRowConverter;
import com.infinityworks.webapp.service.client.VotersByStreet;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.ConstituencyBuilder.constituency;
import static com.infinityworks.webapp.testsupport.builder.PropertyBuilder.property;
import static com.infinityworks.webapp.testsupport.builder.VoterBuilder.voter;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PdfRendererTest {
    private PdfRenderer underTest = new PdfRenderer(new PropertyToRowConverter(), new TableBuilder());

    @Test
    public void generatesTablesFromVoters() throws Exception {
        List<VotersByStreet> votersByStreets = asList(street1(), street2());

        List<GeneratedPdfTable> tables = underTest.generatePDF(
                votersByStreets,
                ward().withWardName("Henley").build(),
                constituency().withName("Coventry South").build());

        assertThat(tables.size(), is(2));
        assertThat(tables.get(0).getMainStreetName(), is("Street1"));
        assertThat(tables.get(1).getMainStreetName(), is("Street2"));

        assertThat(tables.get(0).getConstituencyName(), is("Coventry South"));
        assertThat(tables.get(1).getConstituencyName(), is("Coventry South"));

        assertThat(tables.get(0).getWardName(), is("Henley"));
        assertThat(tables.get(1).getWardName(), is("Henley"));
    }

    @Test
    public void handlesEmptyVotersByStreets() throws Exception {
        List<VotersByStreet> votersByStreets = emptyList();

        List<GeneratedPdfTable> tables = underTest.generatePDF(votersByStreets,
                ward().withWardName("Henley").build(),
                constituency().withName("Coventry South").build());

        assertThat(tables, is(empty()));
    }

    public VotersByStreet street1() {
        return new VotersByStreet(asList(
                property().withBuildingNumber("1").withMainStreet("Street1").withVoters(asList(
                        voter().withFirstName("David").build(),
                        voter().withFirstName("Sam").build()
                )).build(),
                property().withBuildingNumber("2").withMainStreet("Street1").withVoters(asList(
                        voter().withFirstName("Amy").build(),
                        voter().withFirstName("Paul").build()
                )).build(),
                property().withBuildingNumber("3").withMainStreet("Street1").withVoters(singletonList(
                        voter().withFirstName("Abdul").build()
                )).build()
        ));
    }

    public VotersByStreet street2() {
        return new VotersByStreet(asList(
                property().withBuildingNumber("111").withMainStreet("Street2").withVoters(asList(
                        voter().withFirstName("Javier").build(),
                        voter().withFirstName("Marti").build()
                )).build(),
                property().withBuildingNumber("222").withMainStreet("Street2").withVoters(asList(
                        voter().withFirstName("Selina").build(),
                        voter().withFirstName("Pedro").build()
                )).build(),
                property().withBuildingNumber("333").withMainStreet("Street2").withVoters(singletonList(
                        voter().withFirstName("Carlos").build()
                )).build()
        ));
    }
}