package com.infinityworks.pdfgen;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;

@Component
public class TableBuilder {
    private static final Logger log = LoggerFactory.getLogger(TableBuilder.class);
    private static final Font dataFont = new Font(HELVETICA, 11);
    private static final Font headerFont = new Font(HELVETICA, 10);
    private static final BaseColor LIGHT_GREY = new BaseColor(215, 215, 215);
    private static final BaseColor LIGHTER_GREY = new BaseColor(235, 235, 235);

    private PdfPCell createHeaderCell(String content, int rotation) {
        PdfPCell header = new PdfPCell(new Phrase(content, headerFont));
        header.setRotation(rotation);
        header.setBackgroundColor(LIGHTER_GREY);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        return header;
    }

    private PdfPCell createDataCell(String content) {
        PdfPCell like = new PdfPCell(new Phrase(content, dataFont));
        like.setVerticalAlignment(Element.ALIGN_MIDDLE);
        like.setHorizontalAlignment(Element.ALIGN_CENTER);
        return like;
    }

    private void generateTableHeaders(PdfPTable table) {
        table.addCell(createHeaderCell("House #", TableConfig.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Name", TableConfig.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Tel No.", TableConfig.HORIZONTAL_TEXT_ANGLE));

        PdfPCell likelihood = createHeaderCell("Voting\nLikelihood", TableConfig.HORIZONTAL_TEXT_ANGLE);
        likelihood.setBackgroundColor(LIGHT_GREY);

        table.addCell(likelihood);
        table.addCell(createHeaderCell("Cost", TableConfig.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Sovereignty", TableConfig.HORIZONTAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Border", TableConfig.HORIZONTAL_TEXT_ANGLE));

        PdfPCell intention = createHeaderCell("Voting\nIntention", TableConfig.HORIZONTAL_TEXT_ANGLE);
        intention.setBackgroundColor(LIGHT_GREY);

        table.addCell(intention);
        table.addCell(createHeaderCell("Has PV", TableConfig.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Wants PV", TableConfig.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Needs Lift", TableConfig.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Poster", TableConfig.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Dead", TableConfig.VERTICAL_TEXT_ANGLE));
        table.addCell(createHeaderCell("Roll #", TableConfig.HORIZONTAL_TEXT_ANGLE));
    }

    public Optional<GeneratedPdfTable> generateTableRows(List<ElectorRow> rows, String mainStreetName, String wardName, String constituencyName) {
        if (rows.isEmpty()) {
            return Optional.empty();
        }

        PdfPTable table = new PdfPTable(TableConfig.NUM_COLUMNS);
        table.setWidthPercentage(TableConfig.TABLE_WIDTH_PERCENTAGE);
        try {
            table.setWidths(TableConfig.COLUMN_WIDTHS);
        } catch (DocumentException e) {
            log.error("Incorrect table configuration");
            throw new IllegalStateException(e);
        }
        table.setHeaderRows(TableConfig.NUM_HEADER_ROWS);
        generateTableHeaders(table);

        String prevHouse = null;
        for (ElectorRow row : rows) {
            prevHouse = createRow(table, prevHouse, row);
        }

        return Optional.of(new GeneratedPdfTable(table, mainStreetName, wardName, constituencyName));
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

        table.addCell(createDataCell(row.getHouse()));
        table.addCell(createDataCell(row.getName()));
        table.addCell(createDataCell(row.getTelephone()));

        PdfPCell likelihood = new PdfPCell(new Phrase(row.getLikelihood()));
        likelihood.setBackgroundColor(LIGHT_GREY);
        table.addCell(likelihood);

        table.addCell(createDataCell(row.getIssue1()));
        table.addCell(createDataCell(row.getIssue2()));
        table.addCell(createDataCell(row.getIssue3()));

        PdfPCell support = new PdfPCell(new Phrase(row.getSupport()));
        support.setBackgroundColor(LIGHT_GREY);
        table.addCell(support);

        table.addCell(createDataCell(row.getHasPV()));
        table.addCell(createDataCell(row.getWantsPV()));
        table.addCell(createDataCell(row.getNeedsLift()));
        table.addCell(createDataCell(row.getPoster()));
        table.addCell(createDataCell(row.getDeceased()));
        table.addCell(createDataCell(row.getErn()));
        return prevHouse;
    }

    /**
     * Adds an empty black row to signify that the house has changed
     */
    private void addChangeRow(PdfPTable table) {
        IntStream.range(0, TableConfig.NUM_COLUMNS)
                .forEach(i -> {

                    PdfPCell cell = new PdfPCell();
                    cell.setFixedHeight(1);
                    cell.setBackgroundColor(BaseColor.BLACK);
                    table.addCell(cell);
                });

    }
}
