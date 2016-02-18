package com.infinityworks.pdfgen;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DocumentBuilder {
    private final Logger log = LoggerFactory.getLogger(DocumentBuilder.class);

    public ByteArrayOutputStream buildPages(List<GeneratedPdfTable> pdfTables) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(),
                TableConfig.PAGE_MARGIN_LEFT,
                TableConfig.PAGE_MARGIN_RIGHT,
                TableConfig.PAGE_MARGIN_TOP,
                TableConfig.PAGE_MARGIN_BOTTOM);
        PdfWriter writer;
        try {
            writer = PdfWriter.getInstance(document, os);
        } catch (DocumentException e) {
            log.error("Failed to create PDF writer", e);
            throw new IllegalStateException(e);
        }
        StaticContentRenderer staticContentRenderer = new StaticContentRenderer();
        writer.setPageEvent(staticContentRenderer);
        document.open();

        for (GeneratedPdfTable table : pdfTables) {
            try {
                document.add(table.getTable());
                // TODO add street label
            } catch (DocumentException e) {
                log.error("Failed to add page to document", e);
                throw new IllegalStateException(e);
            }
            document.newPage();
        }

        document.close();
        return os;
    }
}
