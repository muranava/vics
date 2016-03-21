package com.infinityworks.webapp.pdf;

import com.infinityworks.webapp.converter.PropertyToRowsConverter;
import com.infinityworks.webapp.paf.dto.ImmutableProperty;
import com.infinityworks.webapp.paf.dto.Property;
import com.infinityworks.webapp.pdf.TablePropertyAccessor.Column;
import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.webapp.pdf.TablePropertyAccessor.getCellText;
import static com.infinityworks.webapp.testsupport.Fixtures.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PdfRendererTest {
    private TableBuilder tableBuilder = new TableBuilder(new CanvassTableConfig());
    private PDFTableGenerator underTest = new PdfJetTableGenerator(new PropertyToRowsConverter());

    @Test
    public void generatesTablesFromVoters() throws Exception {
        List<List<Property>> votersByStreets = asList(street1(), street2());

        List<GeneratedPdfTable> tables = underTest.generateTables(tableBuilder,
                votersByStreets, "E0900134", "Henley", "Coventry South");

        assertThat(tables.size(), is(3));
        assertThat(tables.get(0).street(), is("Street1, CV2 3ER"));
        assertThat(tables.get(1).street(), is("Street2, CV2 3ER"));

        assertThat(tables.get(0).constituencyName(), is("Coventry South"));
        assertThat(tables.get(1).constituencyName(), is("Coventry South"));

        assertThat(tables.get(0).wardName(), is("Henley"));
        assertThat(tables.get(1).wardName(), is("Henley"));
    }

    @Test
    public void handlesEmptyProperties() throws Exception {
        List<List<Property>> votersByStreets = emptyList();

        List<GeneratedPdfTable> tables = underTest.generateTables(tableBuilder, votersByStreets, "E0900134",
                "Henley", "Coventry South");

        assertThat(tables, is(empty()));
    }

    @Test
    public void handlesPropertiesWithoutVoters() throws Exception {
        List<List<Property>> someEmptyProperties = singletonList(streetWithPropertiesWithoutAndWithoutVoters());

        List<GeneratedPdfTable> generatedPdfTables = underTest.generateTables(tableBuilder, someEmptyProperties, "E0900134", "Henley", "Coventry South");

        int expectedRows = 3; // header row, plus two rows for voters
        assertThat(generatedPdfTables.get(0).table().getRows().size(), is(expectedRows));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.HOUSE.getColumn()), is("31a"));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.NAME.getColumn()), is("Brown, David"));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.TEL.getColumn()), is(""));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.LIKELIHOOD.getColumn()), is("4"));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.COST.getColumn()), is("X"));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.SOVEREIGNTY.getColumn()), is("X"));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.BORDER.getColumn()), is(""));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.INTENTION.getColumn()), is("2"));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.HAS_PV.getColumn()), is("X"));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.WANTS_PV.getColumn()), is(""));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.LIFT.getColumn()), is(""));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.POSTER.getColumn()), is(""));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.NO_ACCESS.getColumn()), is(""));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.DEAD.getColumn()), is("X"));
        assertThat(getCellText(generatedPdfTables.get(0), 1, Column.ROLL_NUM.getColumn()), is("ZZ-555-2"));

        assertThat(getCellText(generatedPdfTables.get(0), 2, Column.NAME.getColumn()), is("Brown, Sam"));
    }

    public List<Property> streetWithPropertiesWithoutAndWithoutVoters() {
        return asList(
                ImmutableProperty.builder().withHouse("31a").withStreet("Street1").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withPollingDistrict("ZZ").withElectorNumber("555").withElectorSuffix("2").withFullName("Brown, David").withIssues(
                                issuesWithDefaults().withCost(true).withSovereignty(true).build())
                                .withFlags(flagsWithDefaults().withDeceased(true).withHasPV(true).build())
                                .withVoting(votingWithDefaults().withIntention(2).withLikelihood(4).build())
                                .build(),
                        voterWithDefaults().withFullName("Brown, Sam").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street1").withPostCode("CV2 3ER").withVoters(
                        emptyList()
                ).build()
        );
    }

    public List<Property> street1() {
        return asList(
                ImmutableProperty.builder().withStreet("Street1").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withFullName("David").build(),
                        voterWithDefaults().withFullName("Sam").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street1").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withFullName("Amy").build(),
                        voterWithDefaults().withFullName("Paul").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street1").withPostCode("CV2 3ER").withVoters(singletonList(
                        voterWithDefaults().withFullName("Abdul").build()
                )).build()
        );
    }

    public List<Property> street2() {
        return asList(
                ImmutableProperty.builder().withStreet("Street2").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withFullName("Javier").build(),
                        voterWithDefaults().withFullName("Marti").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street2").withPostCode("CV2 3ER").withVoters(asList(
                        voterWithDefaults().withFullName("Selina").build(),
                        voterWithDefaults().withFullName("Pedro").build()
                )).build(),
                ImmutableProperty.builder().withStreet("Street2").withPostCode("CV2 3ER").withVoters(singletonList(
                        voterWithDefaults().withFullName("Carlos").build()
                )).build()
        );
    }
}