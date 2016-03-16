package com.infinityworks.webapp.pdf.renderer;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class LogoRenderer extends PageEventRenderer {
    private final Logger log = LoggerFactory.getLogger(LogoRenderer.class);
    private Image logo;

    public LogoRenderer(URL logoURL) {
        try {
            this.logo = Image.getInstance(logoURL);
            this.logo.scalePercent(8);
        } catch (BadElementException | IOException e) {
            log.error("Failed to load PDF image for page logo");
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (isEnabled()) {
            try {
                if (logo != null) {
                    writer.getDirectContent().addImage(logo);
                }
            } catch (DocumentException e) {
                log.error("Failed to render logo", e);
            }
        }
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        if (logo != null) {
            logo.setAbsolutePosition(33, 515);
        }
    }
}
