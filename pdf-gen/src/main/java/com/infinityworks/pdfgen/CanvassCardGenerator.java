package com.infinityworks.pdfgen;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CanvassCardGenerator {
    private static final int NUM_COLUMNS = 14;
    private static final int[] COLUMN_WIDTHS = {
            50, // house
            50, // name
            50, // tel
            50, // likelihood
            50, // cost
            50, // sovereign
            50, // border
            50, // intention
            20, // has pv
            20, // wants pv
            20, // needs lift
            20, // poster
            20, // dead
            50  // ERN
    };

    private PdfPCell createHeaderCell(String content, int rotation) {
        PdfPCell like = new PdfPCell(new Phrase(content));
        like.setRotation(rotation);
        like.setVerticalAlignment(Element.ALIGN_MIDDLE);
        like.setHorizontalAlignment(Element.ALIGN_CENTER);
        return like;
    }

    class MyFooter extends PdfPageEventHelper {
        Font font = new Font(Font.UNDEFINED, 5, Font.ITALIC);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase header = new Phrase("this is a header", font);
            Phrase footer = new Phrase("this is a footer", font);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    header,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 10, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);
        }
    }


    private void setHeaders(PdfPTable table) {
        table.addCell(createHeaderCell("House #", 0));
        table.addCell(createHeaderCell("Name", 0));
        table.addCell(createHeaderCell("Tel No.", 0));
        table.addCell(createHeaderCell("Voting\nLikelihood", 0));
        table.addCell(createHeaderCell("Cost", 0));
        table.addCell(createHeaderCell("Sovereignty", 0));
        table.addCell(createHeaderCell("Border", 0));
        table.addCell(createHeaderCell("Voting\nIntention", 0));
        table.addCell(createHeaderCell("Has PV", 90));
        table.addCell(createHeaderCell("Wants PV", 90));
        table.addCell(createHeaderCell("Needs Lift", 90));
        table.addCell(createHeaderCell("Poster", 90));
        table.addCell(createHeaderCell("Dead", 90));
        table.addCell(createHeaderCell("Roll #", 0));
    }

    public ByteArrayOutputStream generatePdfTable(List<ElectorRow> rows) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        document.setMargins(0, 0, 60, 0);

        PdfPTable table = new PdfPTable(NUM_COLUMNS);
        table.setWidthPercentage(95);
        table.setWidths(COLUMN_WIDTHS);
        table.setHeaderRows(1);
        setHeaders(table);

        rows.forEach(row -> {
            table.addCell(row.getHouse());
            table.addCell(row.getName());
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell(row.getErn());
        });

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, os);
        MyFooter event = new MyFooter();
        writer.setPageEvent(event);

        document.open();
        document.add(table);
        document.close();
        return os;
    }
}
