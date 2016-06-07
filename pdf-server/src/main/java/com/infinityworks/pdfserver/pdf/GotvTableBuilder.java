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

public class GotvTableBuilder extends TableBuilderTemplate {
    public GotvTableBuilder(PdfTableConfig columnDefinition) {
        super(columnDefinition);
    }

    @Override
    protected void generateTableHeaders(PdfPTable table) {
        table.addCell(createHeaderCell("House #", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Name", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Tel / Email", TableProperties.HORIZONTAL_TEXT_ANGLE));

        PdfPCell intention = createHeaderCell("Voting\nIntention", TableProperties.HORIZONTAL_TEXT_ANGLE);
        intention.setBackgroundColor(LIGHT_GREY);

        table.addCell(intention);
        table.addCell(createHeaderCell("Has PV", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Needs Lift", TableProperties.VERTICAL_TEXT_ANGLE));
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

        PdfPCell support = new PdfPCell(new Phrase(row.support()));
        support.setBackgroundColor(LIGHT_GREY);
        support.setHorizontalAlignment(Element.ALIGN_CENTER);
        support.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(support);

        addDataCell(table, row.hasPV(), Element.ALIGN_CENTER);
        addDataCell(table, row.needsLift(), Element.ALIGN_CENTER);
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
