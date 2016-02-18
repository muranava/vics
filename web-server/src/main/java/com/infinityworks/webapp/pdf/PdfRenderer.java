package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.ElectorRow;
import com.infinityworks.pdfgen.StaticContentRenderer;
import com.infinityworks.pdfgen.TableBuilder;
import com.infinityworks.webapp.converter.PropertyToRowConverter;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.service.client.VotersByStreet;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class PdfRenderer {
    private final Logger log = LoggerFactory.getLogger(PdfRenderer.class);
    private final PropertyToRowConverter propertyToRowConverter;

    @Autowired
    public PdfRenderer(PropertyToRowConverter propertyToRowConverter) {
        this.propertyToRowConverter = propertyToRowConverter;
    }

    public ByteArrayOutputStream createVotersByStreetsPdf(List<VotersByStreet> votersByStreet, Ward ward, Constituency constituency) {
        List<PdfPTable> pdfTables = generateTables(votersByStreet, ward, constituency.getName());
        return buildPages(pdfTables);
    }

    /**
     * build a pdf table for each street in parallel using common thread pool
     */
    private List<PdfPTable> generateTables(List<VotersByStreet> voters, Ward ward, String constituencyName) {
        return voters.parallelStream()
                .map(street -> {
                    String mainStreetName = street.getMainStreetName();
                    TableBuilder tableBuilder = new TableBuilder(mainStreetName, ward.getName(), constituencyName);
                    List<ElectorRow> electors = street.getProperties()
                            .stream()
                            .map(property -> propertyToRowConverter.apply(ward.getCode(), property))
                            .flatMap(Collection::stream)
                            .collect(toList());
                    return tableBuilder.generateTableRows(electors);
                })
                .collect(toList());
    }

    private ByteArrayOutputStream buildPages(List<PdfPTable> pdfTables) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 30, 10, 100, 30);
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

        for (PdfPTable table : pdfTables) {
            try {
                document.add(table);
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
