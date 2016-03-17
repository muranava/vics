package com.infinityworks.webapp.pdf.renderer;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitlePageRenderer extends PdfPageEventHelper {
    private static final Logger log = LoggerFactory.getLogger(TitlePageRenderer.class);

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        try {
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.saveState();
            cb.beginText();
            cb.moveText(100, 565);
            cb.setFontAndSize(bf, 18);
            cb.showText("Vote Leave (title page here)");
            cb.endText();
            cb.restoreState();
        } catch (Exception e) {
            log.error("Error creating title page", e);
        }
    }
}
