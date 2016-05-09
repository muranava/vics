package com.infinityworks.webapp.pdf;

import com.infinityworks.common.lang.Try;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Avery PDF label generator
 * Not thread safe (clients should create a new instance per use)
 * Forked from Sean K Anderson http://datavirtue.com/Software/dev/PDFLabels.java
 */
@NotThreadSafe
class AveryLabelCreator {
    private static final Font DEFAULT_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);
    private final AveryLabelSpec labelSpec;

    private int cellPosInRow = 0;
    private float numLabelsAcrossPage = 0;

    private Document document = new Document(PageSize.A4);

    /**
     * Single PdfPTable to hold and format the labels.  Each call to add() adds a PdfPCell.
     * You need to make sure you specify JVM options to provide adequate memory for large tables.
     * Check JVM tuning on Sun website.
     */
    private PdfPTable table;

    private static final float CELL_LEFT_PADDING = 15.0f;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    AveryLabelCreator(AveryLabelSpec spec) throws FileNotFoundException, DocumentException {
        this.labelSpec = spec;
        init();
    }

    private void init() throws DocumentException, FileNotFoundException {
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        setDocumentMetaData();
        document.open();

        PdfContentByte cb = writer.getDirectContent();

        ColumnText ct = new ColumnText(cb);
        ct.setSimpleColumn(0, labelSpec.topBottomMarginMillis(),
                PageSize.A4.getWidth(),
                PageSize.A4.getHeight() - labelSpec.topBottomMarginMillis() * 2,
                0,
                Element.ALIGN_LEFT);

        numLabelsAcrossPage = (PageSize.A4.getWidth() - (labelSpec.leftRightMarginMillis() * 2)) / labelSpec.labelWidthMillis();
        numLabelsAcrossPage -= numLabelsAcrossPage % 1; // trim the fat

        float[] columns = new float[(int) numLabelsAcrossPage + 1 + ((int) numLabelsAcrossPage)];

        table = new PdfPTable(columns.length);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        computeColumnWidths(columns);
        table.getDefaultCell().setPaddingBottom(0.0f);
        table.getDefaultCell().setPaddingTop(0.0f);
    }

    private void computeColumnWidths(float[] cols) throws DocumentException {
        float sizeOfAllColumns = 0;

        for (int i = 0; i < cols.length; i++) {
            boolean isStartOrEndColumn = i == 0 || i == cols.length - 1;
            if (isStartOrEndColumn) {
                cols[i] = labelSpec.leftRightMarginMillis();
                sizeOfAllColumns += labelSpec.leftRightMarginMillis();
                continue;
            }

            boolean isGapColumn = (i + 1) % 2 != 0 && i != cols.length - 1;
            if (isGapColumn) {
                cols[i] = labelSpec.labelGapMillis();
                sizeOfAllColumns += labelSpec.labelGapMillis();
            } else {
                cols[i] = labelSpec.labelWidthMillis();
                sizeOfAllColumns += labelSpec.labelWidthMillis();
            }
        }

        table.setTotalWidth(sizeOfAllColumns);  // tried removing this, not good
        table.setWidths(cols);
        table.setLockedWidth(true);
    }

    private void setDocumentMetaData() {
        document.addAuthor("Vote Leave");
        document.addCreationDate();
        document.addSubject("Voter Address Labels");
    }

    public void addCellToTable(PdfPTable table) {
        cellPosInRow++;

        this.table.addCell("");
        this.table.addCell(table);
        if (cellPosInRow == numLabelsAcrossPage) {
            this.table.addCell("");  // at the end
        }

        if (cellPosInRow == numLabelsAcrossPage) {
            cellPosInRow = 0;  // if at the end reset the cellPosInRow
        }
    }

    public void createLabel(String label) {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell(new Phrase(label, DEFAULT_FONT));
        cell.setFixedHeight(labelSpec.labelHeightMillis());
        formatCell(cell);
        table.addCell(cell);

        addCellToTable(table);
    }

    private void formatCell(PdfPCell cell) {
        cell.setPaddingLeft(CELL_LEFT_PADDING);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
    }

    public Try<ByteArrayOutputStream> finish() {
        fillRemainingCellsWithEmptyLabels();
        return createDocument();
    }

    private void fillRemainingCellsWithEmptyLabels() {
        if (cellPosInRow < numLabelsAcrossPage) {
            for (int i = cellPosInRow; i < numLabelsAcrossPage; i++) {
                table.addCell("");
                table.addCell("");
                cellPosInRow++;
            }
            table.addCell("");
        }
    }

    private Try<ByteArrayOutputStream> createDocument() {
        try {
            document.add(table);
            document.close();
            return Try.of(() -> outputStream);
        } catch (DocumentException e) {
            return Try.failure(e);
        }
    }
}
