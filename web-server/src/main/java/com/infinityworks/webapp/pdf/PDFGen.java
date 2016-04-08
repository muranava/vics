package com.infinityworks.webapp.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Avery PDF label generator
 *
 * Not thread safe (create a new instance per use)
 *
 * @author steinfletcher <steinfletcher@gmail.com>
 *  Forked from Sean K Anderson http://datavirtue.com/Software/dev/PDFLabels.java
 */
public class PDFGen {
    private float labelHeight = 1.0f * INCHES_TO_POINTS_CONVERSION;
    private float labelWidth = 2.6f * INCHES_TO_POINTS_CONVERSION;
    private float leftRightMargin = .17f * INCHES_TO_POINTS_CONVERSION;  // The PdfPCells are left-padded by add(PdfPCell cell)  to 8.0f points
    private float topBottomMargin = .50f * INCHES_TO_POINTS_CONVERSION;
    private float labelGap = .17f * INCHES_TO_POINTS_CONVERSION;

    private static final int INCHES_TO_POINTS_CONVERSION = 72;
    private static final Font DEFAULT_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);

    private int cellPosInRow = 0;
    private float across = 0;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private String fileName = "Labels.pdf";

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

    /**
     * Custom label dimensions - in inches.
     * Creates a new instance of PDFLabels.
     */
    public PDFGen(float labelHeight,
                  float labelWidth,
                  float leftRightMargin,
                  float topBottomMargin,
                  float labelGap,
                  String file) throws FileNotFoundException, DocumentException {

        /* Multiply by 72 to convert inches to points */
        this.labelHeight = labelHeight * INCHES_TO_POINTS_CONVERSION;
        this.labelWidth = labelWidth * INCHES_TO_POINTS_CONVERSION;
        this.leftRightMargin = leftRightMargin * INCHES_TO_POINTS_CONVERSION;
        this.topBottomMargin = topBottomMargin * INCHES_TO_POINTS_CONVERSION;
        this.labelGap = labelGap * INCHES_TO_POINTS_CONVERSION;
        this.fileName = file;

        init();
    }

    /**
     * Custom label dimensions - in inches.
     * Creates a new instance of PDFLabels.
     */
    public PDFGen(String fileName) throws FileNotFoundException, DocumentException {
        this.fileName = fileName;
        init();
    }

    public void main(String[] args) throws DocumentException, FileNotFoundException {
        PDFGen pdfGen = new PDFGen("labels1.pdf"); // 5160 || 8160
        pdfGen.setAlignment(9, 1); // sets the default alignment properties for the cells
        String text1 = "Mark Cooper" + LINE_SEPARATOR + "32 Regent Street" + LINE_SEPARATOR + "City of Westminster" + LINE_SEPARATOR + "London" + LINE_SEPARATOR +  "KT6 3UB";
        String text2 = "Sean Anderson" + LINE_SEPARATOR + "138 Seven Kings Way" + LINE_SEPARATOR + "Kingston Upon Thames" + LINE_SEPARATOR + "KT6 3UB";

        for (int r = 0; r < 300; r++) {
            if (r % 2 == 0) {
                pdfGen.createLabel(text1);
            } else {
                pdfGen.createLabel(text2);
            }
        }
        pdfGen.finish();
    }

    private void init() throws DocumentException, FileNotFoundException {
        FileOutputStream outputStream = new FileOutputStream(fileName);
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

    public void add(PdfPTable nt) {
        /* adds the actual cell to the table  */
        /* Every add() calls this method */
        cellPosInRow++;  // 'int cellPosInRow' keeps track of how many times
        // this method is called within a range of X (X = across)

        table.addCell("");
        table.addCell(nt);
        if (cellPosInRow == across) {
            table.addCell("");  //at the end
        }

        if (cellPosInRow == across) {
            cellPosInRow = 0;  // if at the end reset the cellPosInRow
        }
    }

    /**
     * Adds a cell
     * TODO > Remove empty cell creation whilst preserving dimensions (this postnet was deleted from the original
     * TODO > but removing it causes the dimensions to change)
     */
    private void createLabel(String label) {
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

    private void writeLabels() throws DocumentException {
        document.add(table);
    }

    /**
     * Call finish() when you are done adding labels.
     * Catch the boolean return value to make sure everything went as planned.
     */
    private void finish() throws DocumentException {
        /* Fill out lat row */
        if (cellPosInRow < across) {
            for (int i = cellPosInRow; i < across; i++) {
                table.addCell("");
                table.addCell("");
                cellPosInRow++;
            }
            table.addCell("");  // last cell
        }

        writeLabels();
        document.close();
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
