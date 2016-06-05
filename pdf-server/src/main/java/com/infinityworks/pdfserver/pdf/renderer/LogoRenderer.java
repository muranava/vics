package com.infinityworks.pdfserver.pdf.renderer;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class LogoRenderer extends PdfPageEventHelper {
    private final Logger log = LoggerFactory.getLogger(LogoRenderer.class);
    private final int xPos;
    private final int yPos;
    private Image logo;

    public LogoRenderer(URL logoURL, int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;

        try {
            this.logo = Image.getInstance(logoURL);
            this.logo.scalePercent(8);
        } catch (BadElementException | IOException e) {
            log.error("Failed to load PDF image for page logo");
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            if (logo != null) {
                writer.getDirectContent().addImage(logo);
            }
        } catch (DocumentException e) {
            log.error("Failed to render logo", e);
        }
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        if (logo != null) {
            logo.setAbsolutePosition(xPos, yPos);
        }
    }
}
