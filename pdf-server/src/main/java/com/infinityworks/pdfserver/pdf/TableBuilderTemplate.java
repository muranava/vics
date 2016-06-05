package com.infinityworks.pdfserver.pdf;

import com.infinityworks.pdfserver.pdf.model.ElectorRow;
import com.infinityworks.pdfserver.pdf.model.GeneratedPdfTable;
import com.infinityworks.pdfserver.pdf.model.ImmutableGeneratedPdfTable;
import com.infinityworks.pdfserver.pdf.renderer.PdfTableGenerator;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static com.infinityworks.common.lang.StringExtras.isNullOrEmpty;
import static com.lowagie.text.Font.HELVETICA;

public abstract class TableBuilderTemplate implements PdfTableGenerator {
    private static final Logger log = LoggerFactory.getLogger(CanvassTableBuilder.class);
    private static final Font dataFont = new Font(HELVETICA, 11);
    private static final Font headerFont = new Font(HELVETICA, 10);
    private static final Font CLEAR = new Font(HELVETICA, 10, 1, Color.WHITE);
    static final Color LIGHT_GREY = new Color(215, 215, 215);
    private static final Color LIGHTER_GREY = new Color(235, 235, 235);
    private final PdfTableConfig columnDefinition;

    private static final String EMPTY_CELL = "";
    private static final PdfPCell CHANGE_CELL = new PdfPCell();
    static final Phrase EMPTY_CONTENT = new Phrase("N/A", CLEAR);

    static {
        CHANGE_CELL.setFixedHeight(TableProperties.HOUSE_CHANGE_ROW_HEIGHT);
        CHANGE_CELL.setBackgroundColor(Color.BLACK);
    }

    protected TableBuilderTemplate(PdfTableConfig columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    @Override
    public Optional<GeneratedPdfTable> generateTableRows(List<ElectorRow> rows, String mainStreetName, String wardName, String wardCode, String constituencyName) {
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        PdfPTable table = generateTableAndHeaders();
        String prevHouse = null;
        for (ElectorRow row : rows) {
            prevHouse = createRow(table, prevHouse, row);
        }

        ImmutableGeneratedPdfTable pdfTable = ImmutableGeneratedPdfTable.builder()
                .withTable(table)
                .withStreet(mainStreetName)
                .withWardCode(wardCode)
                .withWardName(wardName)
                .withConstituencyName(constituencyName)
                .build();
        return Optional.of(pdfTable);
    }

    protected abstract String createRow(PdfPTable table, String prevHouse, ElectorRow row);

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

    PdfPCell createHeaderCell(String content, int rotation) {
        PdfPCell header = new PdfPCell(new Phrase(content, headerFont));
        header.setRotation(rotation);
        header.setBackgroundColor(LIGHTER_GREY);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        return header;
    }

    void addDataCell(PdfPTable table, String content, int alignment) {
        if (isNullOrEmpty(content)) {
            table.addCell(EMPTY_CELL);
        } else {
            PdfPCell cell = new PdfPCell(new Phrase(content, dataFont));
            cell.setPadding(TableProperties.CELL_PADDING);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(alignment);
            table.addCell(cell);
        }
    }

    protected abstract void generateTableHeaders(PdfPTable table);

    void addChangeRow(PdfPTable table) {
        for (int i = 0; i < columnDefinition.getNumColumns(); i++) {
            table.addCell(CHANGE_CELL);
        }
    }
}
