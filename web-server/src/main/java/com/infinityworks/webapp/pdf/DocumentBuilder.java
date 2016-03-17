package com.infinityworks.webapp.pdf;

import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import com.infinityworks.webapp.pdf.renderer.FlagsKeyRenderer;
import com.infinityworks.webapp.pdf.renderer.LogoRenderer;
import com.infinityworks.webapp.pdf.renderer.PageInfoRenderer;
import com.infinityworks.webapp.pdf.renderer.TitlePageRenderer;
import com.infinityworks.webapp.rest.dto.Flags;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import net.logstash.logback.encoder.org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Builds the PDF document containing the electors by street
 * from a collection of {@link GeneratedPdfTable} tables.
 */
public class DocumentBuilder {
    private final Logger log = LoggerFactory.getLogger(DocumentBuilder.class);
    private final LogoRenderer logoRenderer;
    private final PdfTableConfig tableConfig;
    private byte[] coverPage;
    private final FlagsKeyRenderer flagsKeyRenderer = new FlagsKeyRenderer();

    public DocumentBuilder(LogoRenderer logoRenderer, PdfTableConfig tableConfig) {
        this.logoRenderer = logoRenderer;
        this.tableConfig = tableConfig;

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("pdf/canvass_cover.pdf");
        try {
            coverPage = IOUtils.toByteArray(resourceAsStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load cover page");
        }
    }

    /**
     * Builds a PDF as a byte stream from the given tables
     *
     * @param pdfTables the contents of the data tables
     * @param flags     filter criteria
     * @return the generated PDF document as a byte stream
     */
    public ByteArrayOutputStream buildPdfPages(List<GeneratedPdfTable> pdfTables, Flags flags) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document document = createDocument();

        PageInfoRenderer pageInfoRenderer = new PageInfoRenderer(flagsKeyRenderer, flags);
        pageInfoRenderer.setRenderLikelihoodLegend(tableConfig.showLikelihoodLegend());

        PdfWriter writer;
        try {
            writer = PdfWriter.getInstance(document, os);
            writer.setFullCompression();
        } catch (DocumentException e) {
            log.error("Failed to create PDF writer");
            throw new IllegalStateException(e);
        }
        writer.setPageEvent(pageInfoRenderer);
        writer.setPageEvent(logoRenderer);

        document.open();
        document.newPage();

        renderTables(pdfTables, document, pageInfoRenderer);
        document.close();

        try {
            return PdfUtil.mergePdfs(asList(coverPage, os.toByteArray()));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to render doc", e);
        }
    }

    private void renderTables(List<GeneratedPdfTable> pdfTables, Document document, PageInfoRenderer pageInfoRenderer) {
        for (GeneratedPdfTable table : pdfTables) {
            try {
                pageInfoRenderer.setStreet(table.getStreet());
                pageInfoRenderer.setConstituencyName(table.getConstituencyName());
                pageInfoRenderer.setWardName(table.getWardName());
                document.add(table.getTable());
            } catch (DocumentException e) {
                log.error("Failed to add page to document");
                throw new IllegalStateException(e);
            }
            document.newPage();
        }
    }

    private Document createDocument() {
        return new Document(PageSize.A4.rotate(),
                TableProperties.PAGE_MARGIN_LEFT,
                TableProperties.PAGE_MARGIN_RIGHT,
                TableProperties.PAGE_MARGIN_TOP,
                TableProperties.PAGE_MARGIN_BOTTOM);
    }
}
