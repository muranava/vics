package com.infinityworks.pdfgen;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;

public class TableBuilder {
    private final Logger log = LoggerFactory.getLogger(TableBuilder.class);
    private static Font dataFont = new Font(HELVETICA, 11);
    private static Font headerFont = new Font(HELVETICA, 10);
    private static final BaseColor LIGHT_GREY = new BaseColor(215, 215, 215);
    private static final BaseColor LIGHTER_GREY = new BaseColor(235, 235, 235);
    private static final int NUM_COLUMNS = 14;
    private final String wardName;
    private final String constituencyName;
    private final String streetName;
    private String prevHouse = null;

    private static final int[] COLUMN_WIDTHS = {
            40, // house
            75, // name
            65, // tel
            40, // likelihood
            30, // iss 1
            45, // iss 2
            30, // iss 3
            40, // intention
            20, // has pv
            20, // wants pv
            20, // needs lift
            20, // poster
            20, // dead
            85  // ERN
    };

    public TableBuilder(String streetName, String wardName, String constituencyName) {
        this.streetName = streetName;
        this.wardName = wardName;
        this.constituencyName = constituencyName;
    }

    private void renderMetaInfo(String wardName, String constituencyName, String streetName) {

    }

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

    private void setHeaders(PdfPTable table) {
        table.addCell(createHeaderCell("House #", 0));
        table.addCell(createHeaderCell("Name", 0));
        table.addCell(createHeaderCell("Tel No.", 0));

        PdfPCell likelihood = createHeaderCell("Voting\nLikelihood", 0);
        likelihood.setBackgroundColor(LIGHT_GREY);

        table.addCell(likelihood);
        table.addCell(createHeaderCell("Cost", 0));
        table.addCell(createHeaderCell("Sovereignty", 0));
        table.addCell(createHeaderCell("Border", 0));

        PdfPCell intention = createHeaderCell("Voting\nIntention", 0);
        intention.setBackgroundColor(LIGHT_GREY);

        table.addCell(intention);
        table.addCell(createHeaderCell("Has PV", 90));
        table.addCell(createHeaderCell("Wants PV", 90));
        table.addCell(createHeaderCell("Needs Lift", 90));
        table.addCell(createHeaderCell("Poster", 90));
        table.addCell(createHeaderCell("Dead", 90));
        table.addCell(createHeaderCell("Roll #", 0));
    }

    public PdfPTable generateTableRows(List<ElectorRow> rows) {
        renderMetaInfo(wardName, constituencyName, streetName);

        PdfPTable table = new PdfPTable(NUM_COLUMNS);
        table.setWidthPercentage(95);
        try {
            table.setWidths(COLUMN_WIDTHS);
        } catch (DocumentException e) {
            log.error("Incorrect table configuration");
            throw new IllegalStateException(e);
        }
        table.setHeaderRows(1);
        setHeaders(table);

        rows.forEach(row -> {
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
        });

        return table;
    }

    private void addChangeRow(PdfPTable table) {
        IntStream.range(0, NUM_COLUMNS)
                 .forEach(i -> {

                     PdfPCell cell = new PdfPCell();
                     cell.setFixedHeight(1);
                     cell.setBackgroundColor(BaseColor.BLACK);
                     table.addCell(cell);
                 });

    }
}
