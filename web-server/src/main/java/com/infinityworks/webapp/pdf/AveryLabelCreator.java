package com.infinityworks.webapp.pdf;

import com.infinityworks.common.lang.Try;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Avery PDF label generator
 * Not thread safe (create a new instance per use)
 * Forked from Sean K Anderson http://datavirtue.com/Software/dev/PDFLabels.java
 */
@NotThreadSafe
class AveryLabelCreator {
    private float labelHeight = 1.0f * INCHES_TO_POINTS_CONVERSION;
    private float labelWidth = 2.6f * INCHES_TO_POINTS_CONVERSION;
    private float leftRightMargin = .17f * INCHES_TO_POINTS_CONVERSION;  // The PdfPCells are left-padded by add(PdfPCell cell)  to 8.0f points
    private float topBottomMargin = .50f * INCHES_TO_POINTS_CONVERSION;
    private float labelGap = .17f * INCHES_TO_POINTS_CONVERSION;

    private static final int INCHES_TO_POINTS_CONVERSION = 72;
    private static final Font DEFAULT_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);

    private int cellPosInRow = 0;
    private float across = 0;

    private Document document = new Document(PageSize.LETTER);

    /**
     * Single PdfPTable to hold and format the labels.  Each call to add() adds a PdfPCell.
     * You need to make sure you specify JVM options to provide adequate memory for large tables.
     * Check JVM tuning on Sun website.
     */
    private PdfPTable table;

    /**
     * Stores vertical alignment value for PdfPCells, see: setAlignment(int vertical, int horizontal)
     */
    private int verticalAlignment = Element.ALIGN_MIDDLE;

    /**
     * Stores horizontal alignment value for PdfPCells, see: setAlignment(int vertical, int horizontal)
     */
    private int horizontalAlignment = Element.ALIGN_LEFT;

    private boolean showBorders;

    private static final float CELL_LEFT_PADDING = 8.0f;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    AveryLabelCreator() throws FileNotFoundException, DocumentException {
        init();
    }

    private void init() throws DocumentException, FileNotFoundException {
        this.setAlignment(9, 1);

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        setDocumentMetaData();
        document.open();

        PdfContentByte cb = writer.getDirectContent();

        ColumnText ct = new ColumnText(cb);
        ct.setSimpleColumn(0, topBottomMargin, PageSize.LETTER.getWidth(), PageSize.LETTER.getHeight() - topBottomMargin * 2, 0, Element.ALIGN_MIDDLE);

        across = (PageSize.LETTER.getWidth() - (leftRightMargin * 2)) / labelWidth;  // how many labels across based on supplied dims
        across -= across % 1; // trim the fat

        float[] columns = new float[(int) across + 1 + ((int) across)];

        table = new PdfPTable(columns.length); // table with gaps and margins included
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
                cols[i] = leftRightMargin;
                sizeOfAllColumns += leftRightMargin;
                continue;
            }

            boolean isGapColumn = (i + 1) % 2 != 0 && i != cols.length - 1;
            if (isGapColumn) {
                cols[i] = labelGap;
                sizeOfAllColumns += labelGap;
            } else {
                cols[i] = labelWidth;
                sizeOfAllColumns += labelWidth;
            }
        }

        table.setTotalWidth(sizeOfAllColumns);  // tried removing this, not good
        table.setWidths(cols);
        table.setLockedWidth(true);
    }

    private void setDocumentMetaData() {
        document.addAuthor("Vote Leave");  // change this or create fields with get/set to change this
        document.addCreationDate();         // these statements must be called or set before calling open()
        document.addSubject("Nevitium Labels - datavirtue.com");
    }

    /**
     * Adds the cell to the table
     *
     * @param table the pdf table holding all labels
     */
    public void add(PdfPTable table) {
        cellPosInRow++;

        this.table.addCell("");
        this.table.addCell(table);
        if (cellPosInRow == across) {
            this.table.addCell("");  // at the end
        }

        if (cellPosInRow == across) {
            cellPosInRow = 0;  // if at the end reset the cellPosInRow
        }
    }

    public void createLabel(String label) {
        PdfPTable table = new PdfPTable(1);

        PdfPCell cell = new PdfPCell(new Phrase(label, DEFAULT_FONT));
        cell.setFixedHeight(labelHeight * .85f);
        formatCell(cell);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", DEFAULT_FONT));
        cell.setFixedHeight(labelHeight * .15f);
        formatCell(cell);
        table.addCell(cell);
        table.setTotalWidth(labelWidth);

        add(table);
    }

    /**
     * Set the fixedHeight before calling this method.
     */
    private void formatCell(PdfPCell cell) {
        cell.setPaddingLeft(CELL_LEFT_PADDING);
        cell.setVerticalAlignment(verticalAlignment);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setBorder(Rectangle.NO_BORDER);  //show table borders in DEBUG mode
    }

    /**
     * Call finish() when you are done adding labels.
     * Catch the boolean return value to make sure everything went as planned.
     */
    Try<ByteArrayOutputStream> finish() {
        /* Fill out lat row */
        if (cellPosInRow < across) {
            for (int i = cellPosInRow; i < across; i++) {
                table.addCell("");
                table.addCell("");
                cellPosInRow++;
            }
            table.addCell("");  // last cell
        }

        try {
            document.add(table);
            document.close();
            return Try.of(() -> outputStream);
        } catch (DocumentException e) {
            return Try.failure(e);
        }
    }

    /**
     * Changes alignment values for the cells added to the table.
     * Call once or before each add method call.
     */
    private void setAlignment(int vertical, int horizontal) {
        if (horizontal != 9) {
            horizontalAlignment = horizontal;
        }
        if (vertical != 9) {
            verticalAlignment = vertical;
        }
    }
}
