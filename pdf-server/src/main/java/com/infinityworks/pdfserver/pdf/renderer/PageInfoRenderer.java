package com.infinityworks.pdfserver.pdf.renderer;

import com.infinityworks.pdfserver.controller.dto.Flags;
import com.infinityworks.pdfserver.pdf.PdfTableConfig;
import com.infinityworks.pdfserver.pdf.model.Point;
import com.infinityworks.pdfserver.pdf.model.RectPoint;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.lowagie.text.Font.HELVETICA;

public class PageInfoRenderer extends PdfPageEventHelper {
    private static final Logger log = LoggerFactory.getLogger(PageInfoRenderer.class);
    private static Font font = new Font(HELVETICA, 9);
    private static final String FOOTER_TEXT_1 =
            "Promoted by Matthew Elliott on behalf of Vote Leave Limited, both of Westminster Tower, 3 Albert Embankment, London SE1 7SP." +
                    "  Printed by Vote Leave Limited.";
    private static final String FOOTER_TEXT_GOTV_1 =
            "Promoted by Matthew Elliott on behalf of Vote Leave Limited, both of Westminster Tower, 3 Albert Embankment, London SE1 7SP.";
    private static final String FOOTER_TEXT_2 = "All data is collected in accordance with our privacy policy which can be found at " +
            "http://www.voteleavetakecontrol.org/privacy  %s.";
    private static final String FOOTER_TEXT_GOTV_2 = "All data is collected in accordance with our privacy policy which can be found at " +
            "http://www.voteleavetakecontrol.org/privacy.";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    private static final String LIKELIHOOD_KEY =
            "1 Definitely won't vote" +
                    "\n2 Probably won't vote" +
                    "\n3 Undecided" +
                    "\n4 Probably will vote" +
                    "\n5 Definitely will vote";

    private static final String INTENTION_KEY =
            "1 Definitely Remain" +
                    "\n2 Probably Remain" +
                    "\n3 Undecided" +
                    "\n4 Probably Leave" +
                    "\n5 Definitely Leave";

    private static final String META_TEMPLATE = "Constituency: %s\nWard: %s \nAddress: %s";
    private final FlagsKeyRenderer flagsKeyRenderer;
    private final Flags flags;
    private final PdfTableConfig config;

    private String constituencyName = "";
    private String wardName = "";
    private String street = "";

    public PageInfoRenderer(FlagsKeyRenderer flagsKeyRenderer, Flags flags, PdfTableConfig config) {
        this.flagsKeyRenderer = flagsKeyRenderer;
        this.flags = flags;
        this.config = config;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        createFooter(cb);
        createLogo(cb);
        createIntentionKey(cb);
        if (!config.isGotv()) {
            createLikelihoodKey(cb);
        }
        if (flags != null) {
            createFlagsKey(cb);
        }
        createPageNumber(cb, document, writer);
        createMetaSection(cb);
    }

    private void createPageNumber(PdfContentByte cb, Document document, PdfWriter writer) {
        String page = String.format("Page %d", writer.getPageNumber());
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                new Phrase(page, font),
                document.right() - 50,
                document.bottom() - 15, 0);
    }

    private void createFooter(PdfContentByte cb) {
        String line1 = config.isGotv() ? FOOTER_TEXT_GOTV_1 : FOOTER_TEXT_1;
        Phrase footer1 = new Phrase(line1, font);
        Point pointLine1 = config.footerTextPosition()[0];
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, footer1, pointLine1.getX(), pointLine1.getY(), 0);

        String line2 = config.isGotv() ? FOOTER_TEXT_GOTV_2 : FOOTER_TEXT_2;
        Phrase footer2 = new Phrase(String.format(line2, LocalDateTime.now(ZoneId.of("GMT")).format(formatter)), font);
        Point pointLine2 = config.footerTextPosition()[1];
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, footer2, pointLine2.getX(), pointLine2.getY(), 0);

        if (config.isGotv()) {
            Phrase timestampPhrase = new Phrase(LocalDateTime.now(ZoneId.of("GMT")).format(formatter), font);
            Point timestampPoint = config.footerTextPosition()[1];
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, timestampPhrase, timestampPoint.getX(), 15, 0);
        }
    }

    private void createLogo(PdfContentByte cb) {
        try {
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.saveState();
            cb.beginText();
            cb.moveText(config.logoPosition().getX(), config.logoPosition().getY());
            cb.setFontAndSize(bf, 18);
            cb.showText("Vote Leave");
            cb.endText();
            cb.restoreState();
        } catch (DocumentException | IOException e) {
            log.error("Failed to render logo text", e);
        }
    }

    private void createLikelihoodKey(PdfContentByte cb) {
        ColumnText ct = new ColumnText(cb);
        ct.setText(new Phrase(LIKELIHOOD_KEY, font));
        ct.setSimpleColumn(365, 100, 700, 590);
        try {
            ct.go();
        } catch (DocumentException e) {
            log.error("Failed to render likelihood text", e);
        }
    }

    private void createIntentionKey(PdfContentByte cb) {
        ColumnText ct = new ColumnText(cb);
        ct.setText(new Phrase(INTENTION_KEY, font));
        RectPoint pos = config.intentionTextPosition();
        ct.setSimpleColumn(pos.getLowerLeftX(), pos.getLowerLeftY(), pos.getUpperRightX(), pos.getUpperRightY());
        try {
            ct.go();
        } catch (DocumentException e) {
            log.error("Failed to render intention text", e);
        }
    }

    private void createFlagsKey(PdfContentByte cb) {
        ColumnText ct = new ColumnText(cb);
        Phrase phrase = new Phrase(flagsKeyRenderer.apply(flags), font);
        ct.setText(phrase);
        ct.setSimpleColumn(650, 0, 820, 590);
        try {
            ct.go();
        } catch (DocumentException e) {
            log.error("Failed to flags text", e);
        }
    }

    private void createMetaSection(PdfContentByte cb) {
        ColumnText ct = new ColumnText(cb);
        String format = String.format(META_TEMPLATE, constituencyName, wardName, street);
        ct.setText(new Phrase(format, font));
        RectPoint pos = config.addressTextPosition();
        ct.setSimpleColumn(pos.getLowerLeftX(), pos.getLowerLeftY(), pos.getUpperRightX(), pos.getUpperRightY());
        try {
            ct.go();
        } catch (DocumentException e) {
            log.error("Failed to render intention text", e);
        }
    }

    public void setConstituencyName(String constituencyName) {
        this.constituencyName = constituencyName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
