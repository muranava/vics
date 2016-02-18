package com.infinityworks.pdfgen;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoRenderer extends PdfPageEventHelper {
    private final Image image = Images.logo;
    private final Logger log = LoggerFactory.getLogger(LogoRenderer.class);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            writer.getDirectContent().addImage(image);
        } catch (DocumentException e) {
            log.error("Failed to render logo", e);
        }
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        image.setAbsolutePosition(33, 510);
    }
}
