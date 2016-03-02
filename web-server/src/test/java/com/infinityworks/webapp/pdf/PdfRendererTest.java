package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.TableBuilder;
import com.infinityworks.pdfgen.model.GeneratedPdfTable;
import com.infinityworks.webapp.converter.PropertyToRowConverter;
import com.infinityworks.webapp.paf.dto.ImmutableProperty;
import com.infinityworks.webapp.paf.dto.Property;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.webapp.testsupport.Fixtures.voterWithDefaults;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PdfRendererTest {
    private PDFTableGenerator underTest = new PdfJetTableGenerator(new PropertyToRowConverter(), new TableBuilder());

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
                ImmutableProperty.builder().withStreet("Street1").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withFirstName("David").build(),
                        voterWithDefaults().withFirstName("Sam").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street1").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withFirstName("Amy").build(),
                        voterWithDefaults().withFirstName("Paul").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street1").withPostCode("CV2 3ER").withVoters(singletonList(
                        voterWithDefaults().withFirstName("Abdul").build()
                )).build()
        );
    }

    public List<Property> street2() {
        return asList(
                ImmutableProperty.builder().withStreet("Street2").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withFirstName("Javier").build(),
                        voterWithDefaults().withFirstName("Marti").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street2").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withFirstName("Selina").build(),
                        voterWithDefaults().withFirstName("Pedro").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street2").withPostCode("CV2 3ER").withVoters(singletonList(
                        voterWithDefaults().withFirstName("Carlos").build()
                )).build()
        );
    }
}