package com.infinityworks.webapp.pdf;

import com.infinityworks.common.lang.StringExtras;
import com.infinityworks.webapp.pdf.model.ElectorRow;
import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.infinityworks.common.lang.StringExtras.isNullOrEmpty;
import static com.lowagie.text.Font.HELVETICA;
import static java.util.stream.Collectors.joining;

@Component
public class TableBuilder {
    private static final Logger log = LoggerFactory.getLogger(TableBuilder.class);
    private static final Font dataFont = new Font(HELVETICA, 11);
    private static final Font headerFont = new Font(HELVETICA, 10);
    private static final Font clear = new Font(HELVETICA, 10, 1, Color.WHITE);
    private static final Color LIGHT_GREY = new Color(215, 215, 215);
    private static final Color LIGHTER_GREY = new Color(235, 235, 235);
    private final PdfTableConfig columnDefinition;

    @Autowired
    public TableBuilder(PdfTableConfig columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    private PdfPCell createHeaderCell(String content, int rotation) {
        PdfPCell header = new PdfPCell(new Phrase(content, headerFont));
        header.setRotation(rotation);
        header.setBackgroundColor(LIGHTER_GREY);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        return header;
    }

    private PdfPCell createDataCell(String content, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, dataFont));
        cell.setPadding(3);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private void generateTableHeaders(PdfPTable table) {
        table.addCell(createHeaderCell("House #", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Name", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Contact", TableProperties.HORIZONTAL_TEXT_ANGLE));

        PdfPCell likelihood = createHeaderCell("Voting\nLikelihood", TableProperties.HORIZONTAL_TEXT_ANGLE);
        likelihood.setBackgroundColor(LIGHT_GREY);

        table.addCell(likelihood);
        table.addCell(createHeaderCell("Cost", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Sovereignty", TableProperties.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Border", TableProperties.HORIZONTAL_TEXT_ANGLE));

        PdfPCell intention = createHeaderCell("Voting\nIntention", TableProperties.HORIZONTAL_TEXT_ANGLE);
        intention.setBackgroundColor(LIGHT_GREY);

        table.addCell(intention);
        table.addCell(createHeaderCell("Has PV", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Wants PV", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Needs Lift", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Poster", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("No Access", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Dead", TableProperties.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Roll #", TableProperties.HORIZONTAL_TEXT_ANGLE));
    }

    public Optional<GeneratedPdfTable> generateTableRows(List<ElectorRow> rows, String mainStreetName, String wardName, String wardCode, String constituencyName) {
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        PdfPTable table = generateTableAndHeaders();
        String prevHouse = null;
        for (ElectorRow row : rows) {
            prevHouse = createRow(table, prevHouse, row);
        }

        return Optional.of(new GeneratedPdfTable(table, mainStreetName, wardName, wardCode, constituencyName));
    }

    private PdfPTable generateTableAndHeaders() {
        PdfPTable table = new PdfPTable(columnDefinition.getNumColumns());
        table.setWidthPercentage(TableProperties.TABLE_WIDTH_PERCENTAGE);
        try {
            table.setWidths(columnDefinition.getColumnWidths());
        } catch (DocumentException e) {
            log.error("Incorrect table configuration");
            throw new IllegalStateException(e);
        }
        table.setHeaderRows(TableProperties.NUM_HEADER_ROWS);
        generateTableHeaders(table);
        return table;
    }

    /**
     * Creates a table row from an individual {@link ElectorRow}
     * @return the house that is generated for the current house
     */
    private String createRow(PdfPTable table, String prevHouse, ElectorRow row) {
        boolean houseChanges = prevHouse != null && !Objects.equals(prevHouse, row.getHouse());
        prevHouse = row.getHouse();

        if (houseChanges) {
            addChangeRow(table);
        }

        // workaround to set row height for empty rows (relevant when creating empty pdf page)
        if (isNullOrEmpty(row.getHouse())) {
            table.addCell(new Phrase("-", clear));
        } else {
            table.addCell(createDataCell(row.getHouse(), Element.ALIGN_LEFT));
        }

        table.addCell(createDataCell(row.getName(), Element.ALIGN_LEFT));
            table.addCell(createDataCell(getContact(row), Element.ALIGN_LEFT));

        PdfPCell likelihood = new PdfPCell(new Phrase(row.getLikelihood()));
        likelihood.setBackgroundColor(LIGHT_GREY);
        likelihood.setHorizontalAlignment(Element.ALIGN_CENTER);
        likelihood.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(likelihood);

        table.addCell(createDataCell(row.getIssue1(), Element.ALIGN_CENTER));
        table.addCell(createDataCell(row.getIssue2(), Element.ALIGN_CENTER));
        table.addCell(createDataCell(row.getIssue3(), Element.ALIGN_CENTER));

        PdfPCell support = new PdfPCell(new Phrase(row.getSupport()));
        support.setBackgroundColor(LIGHT_GREY);
        support.setHorizontalAlignment(Element.ALIGN_CENTER);
        support.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(support);

        table.addCell(createDataCell(row.getHasPV(), Element.ALIGN_CENTER));
        table.addCell(createDataCell(row.getWantsPV(), Element.ALIGN_CENTER));
        table.addCell(createDataCell(row.getNeedsLift(), Element.ALIGN_CENTER));
        table.addCell(createDataCell(row.getPoster(), Element.ALIGN_CENTER));
        table.addCell(createDataCell(row.getInaccessible(), Element.ALIGN_CENTER));
        table.addCell(createDataCell(row.getDeceased(), Element.ALIGN_CENTER));
        table.addCell(createDataCell(row.getErn(), Element.ALIGN_LEFT));
        return prevHouse;
    }

    private String getContact(ElectorRow row) {
        return Stream.of(row.getEmail(), row.getTelephone())
                .filter(e -> !StringExtras.isNullOrEmpty(e))
                .collect(joining(", "));
    }

    /**
     * Adds an empty black row to signify that the house has changed
     */
    private void addChangeRow(PdfPTable table) {
        IntStream.range(0, columnDefinition.getNumColumns())
                .forEach(i -> {
                    PdfPCell cell = new PdfPCell();
                    cell.setFixedHeight(0.15f);
                    cell.setBackgroundColor(Color.BLACK);
                    table.addCell(cell);
                });

    }
}
