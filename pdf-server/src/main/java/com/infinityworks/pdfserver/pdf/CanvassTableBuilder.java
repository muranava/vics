package com.infinityworks.pdfserver.pdf;

import com.infinityworks.pdfserver.pdf.model.ElectorRow;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.util.Objects;
import java.util.stream.Stream;

import static com.infinityworks.common.lang.StringExtras.isNullOrEmpty;
import static java.util.stream.Collectors.joining;

public class CanvassTableBuilder extends TableBuilderTemplate {
    public CanvassTableBuilder(PdfTableConfig columnDefinition) {
        super(columnDefinition);
    }

    @Override
    protected void generateTableHeaders(PdfPTable table) {
        table.addCell(createHeaderCell("House #", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Name", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Tel / Email", TableProperties.HORIZONTAL_TEXT_ANGLE));

        PdfPCell likelihood = createHeaderCell("Voting\nLikelihood", TableProperties.HORIZONTAL_TEXT_ANGLE);
        likelihood.setBackgroundColor(LIGHT_GREY);

        table.addCell(likelihood);
        table.addCell(createHeaderCell("Cost", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Control", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Border", TableProperties.HORIZONTAL_TEXT_ANGLE));

        PdfPCell intention = createHeaderCell("Voting\nIntention", TableProperties.HORIZONTAL_TEXT_ANGLE);
        intention.setBackgroundColor(LIGHT_GREY);

        table.addCell(intention);
        table.addCell(createHeaderCell("Has Voted", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Has PV", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Wants PV", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Needs Lift", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Poster", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("No Access", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Dead", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Roll #", TableProperties.HORIZONTAL_TEXT_ANGLE));
    }

    /**
     * Creates a table row from an individual {@link ElectorRow}
     *
     * @return the house that is generated for the current house
     */
    @Override
    public String createRow(PdfPTable table, String prevHouse, ElectorRow row) {
        boolean shouldAddHouseChangeRow = prevHouse != null && !Objects.equals(prevHouse, row.house());
        prevHouse = row.house();

        if (shouldAddHouseChangeRow) {
            addChangeRow(table);
        }

        // workaround to set row height for empty rows (relevant when creating empty pdf page)
        if (isNullOrEmpty(row.house())) {
            table.addCell(EMPTY_CONTENT);
        } else {
            addDataCell(table, row.house(), Element.ALIGN_LEFT);
        }

        addDataCell(table, row.name(), Element.ALIGN_LEFT);
        addDataCell(table, formatContactInfo(row), Element.ALIGN_LEFT);

        PdfPCell likelihood = new PdfPCell(new Phrase(row.likelihood()));
        likelihood.setBackgroundColor(LIGHT_GREY);
        likelihood.setHorizontalAlignment(Element.ALIGN_CENTER);
        likelihood.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(likelihood);

        addDataCell(table, row.issue1(), Element.ALIGN_CENTER);
        addDataCell(table, row.issue2(), Element.ALIGN_CENTER);
        addDataCell(table, row.issue3(), Element.ALIGN_CENTER);

        PdfPCell support = new PdfPCell(new Phrase(row.support()));
        support.setBackgroundColor(LIGHT_GREY);
        support.setHorizontalAlignment(Element.ALIGN_CENTER);
        support.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(support);

        addDataCell(table, row.hasVoted(), Element.ALIGN_CENTER);
        addDataCell(table, row.hasPV(), Element.ALIGN_CENTER);
        addDataCell(table, row.wantsPV(), Element.ALIGN_CENTER);
        addDataCell(table, row.needsLift(), Element.ALIGN_CENTER);
        addDataCell(table, row.poster(), Element.ALIGN_CENTER);
        addDataCell(table, row.inaccessible(), Element.ALIGN_CENTER);
        addDataCell(table, row.deceased(), Element.ALIGN_CENTER);
        addDataCell(table, row.ern(), Element.ALIGN_LEFT);
        return prevHouse;
    }

    private String formatContactInfo(ElectorRow row) {
        return Stream.of(row.email(), row.telephone())
                .filter(e -> !isNullOrEmpty(e))
                .collect(joining(", "));
    }
}
