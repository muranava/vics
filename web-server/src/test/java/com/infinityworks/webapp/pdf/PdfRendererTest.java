package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.TableBuilder;
import com.infinityworks.pdfgen.model.GeneratedPdfTable;
import com.infinityworks.webapp.converter.PropertyToRowConverter;
import com.infinityworks.webapp.paf.dto.Property;
import com.infinityworks.webapp.paf.dto.VotersByStreet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.upstream.PropertyBuilder.property;
import static com.infinityworks.webapp.testsupport.builder.upstream.VoterBuilder.voter;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PdfRendererTest {
    private PDFTableGenerator underTest = new ITextTableGenerator(new PropertyToRowConverter(), new TableBuilder());

    @Test
    public void generatesTablesFromVoters() throws Exception {
        List<List<Property>> votersByStreets = asList(street1(), street2());

        List<GeneratedPdfTable> tables = underTest.generateTables(
                votersByStreets, "E0900134", "Henley", "Coventry South");

        assertThat(tables.size(), is(2));
        assertThat(tables.get(0).getStreet(), is("Street1, CV2 3ER"));
        assertThat(tables.get(1).getStreet(), is("Street2, CV2 3ER"));

        assertThat(tables.get(0).getConstituencyName(), is("Coventry South"));
        assertThat(tables.get(1).getConstituencyName(), is("Coventry South"));

        assertThat(tables.get(0).getWardName(), is("Henley"));
        assertThat(tables.get(1).getWardName(), is("Henley"));
    }

    @Test
    public void handlesEmptyVotersByStreets() throws Exception {
        List<List<Property>> votersByStreets = emptyList();

        List<GeneratedPdfTable> tables = underTest.generateTables(votersByStreets, "E0900134",
                "Henley", "Coventry South");

        assertThat(tables, is(empty()));
    }

    public List<Property> street1() {
        return asList(
                property().withStreet("Street1").withPostCode("CV2 3ER").withVoters(asList(
                        voter().withFirstName("David").build(),
                        voter().withFirstName("Sam").build()
                )).build(),
                property().withStreet("Street1").withPostCode("CV2 3ER").withVoters(asList(
                        voter().withFirstName("Amy").build(),
                        voter().withFirstName("Paul").build()
                )).build(),
                property().withStreet("Street1").withPostCode("CV2 3ER").withVoters(singletonList(
                        voter().withFirstName("Abdul").build()
                )).build()
        );
    }

    public List<Property> street2() {
        return asList(
                property().withStreet("Street2").withPostCode("CV2 3ER").withVoters(asList(
                        voter().withFirstName("Javier").build(),
                        voter().withFirstName("Marti").build()
                )).build(),
                property().withStreet("Street2").withPostCode("CV2 3ER").withVoters(asList(
                        voter().withFirstName("Selina").build(),
                        voter().withFirstName("Pedro").build()
                )).build(),
                property().withStreet("Street2").withPostCode("CV2 3ER").withVoters(singletonList(
                        voter().withFirstName("Carlos").build()
                )).build()
        );
    }
}