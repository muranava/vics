package com.infinityworks.pdfgen;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;

public class StaticContentRenderer extends PdfPageEventHelper {
    private static final Logger log = LoggerFactory.getLogger(StaticContentRenderer.class);
    private static Font font = new Font(HELVETICA, 9);
    private static final String FOOTER_TEXT =
            "All data is collected in accordance with our privacy policy which can be found at " +
                    "http://www.voteleavetakecontrol.org/privacy";

    private static final String LIKELIHOOD_KEY =
            "Voting Likelihood:" +
            "\n1 Definitely won't vote" +
            "\n2 Probably won't vote" +
            "\n3 Undecided" +
            "\n4 Probably will vote" +
            "\n5 Definitely will vote";

    private static final String INTENTION_KEY =
            "Voting Intention:" +
            "\n1 Definitely Remain" +
            "\n2 Probably Remain" +
            "\n3 Undecided" +
            "\n4 Probably Leave" +
            "\n5 Definitely Leave";

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        createFooter(cb, document);
        createLogo(cb);
        createIntentionKey(cb);
        createLikelihoodKey(cb);
    }

    private void createFooter(PdfContentByte cb, Document document) {
        Phrase footer = new Phrase(FOOTER_TEXT, font);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer,
                document.left(),
                document.bottom() - 15, 0);
    }

    private void createLogo(PdfContentByte cb) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            log.error("Failed to render logo text", e);
        }
        cb.saveState();
        cb.beginText();
        cb.moveText(50, 560);
        cb.setFontAndSize(bf, 18);
        cb.showText("Vote Leave");
        cb.endText();
        cb.restoreState();
    }

    private void createLikelihoodKey(PdfContentByte cb) {
        ColumnText ct = new ColumnText(cb);
        ct.setText(new Phrase(LIKELIHOOD_KEY, font));
        ct.setSimpleColumn(new Rectangle(300, 100, 700, 600));
        try {
            ct.go();
        } catch (DocumentException e) {
            log.error("Failed to render likelihood text", e);
        }
    }

    private void createIntentionKey(PdfContentByte cb) {
        ColumnText ct = new ColumnText(cb);
        ct.setText(new Phrase(INTENTION_KEY, font));
        ct.setSimpleColumn(new Rectangle(500, 0, 700, 600));
        try {
            ct.go();
        } catch (DocumentException e) {
            log.error("Failed to render intention text", e);
        }
    }
}
