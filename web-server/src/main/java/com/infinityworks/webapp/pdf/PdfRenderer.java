package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.*;
import com.infinityworks.webapp.converter.PropertyToRowConverter;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.service.client.VotersByStreet;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class PdfRenderer {
    private final Logger log = LoggerFactory.getLogger(PdfRenderer.class);
    private final PropertyToRowConverter propertyToRowConverter;
    private final TableBuilder tableBuilder;
    private final DocumentBuilder documentBuilder;

    @Autowired
    public PdfRenderer(PropertyToRowConverter propertyToRowConverter, DocumentBuilder documentBuilder, TableBuilder tableBuilder) {
        this.propertyToRowConverter = propertyToRowConverter;
        this.documentBuilder = documentBuilder;
        this.tableBuilder = tableBuilder;
    }

    public ByteArrayOutputStream createVotersByStreetsPdf(List<VotersByStreet> votersByStreet, Ward ward, Constituency constituency) {
        log.debug("Generating PDF... ward={} constituency={}", ward.getCode(), constituency.getCode());
        List<GeneratedPdfTable> pdfTables = generateTables(votersByStreet, ward, constituency.getName());
        log.debug("Finished generating PDF... ward={} constituency={}", ward.getCode(), constituency.getCode());
        return documentBuilder.buildPages(pdfTables);
    }

    /**
     * Build a pdf table for each street. Uses a sequential stream to build each street, which is faster than
     * a parallelStream since the number of streets is typically small.
     */
    private List<GeneratedPdfTable> generateTables(List<VotersByStreet> voters, Ward ward, String constituencyName) {
        return voters.stream()
                .map(street -> {
                    String mainStreetName = street.getMainStreetName();
                    List<ElectorRow> electors = street.getProperties()
                            .stream()
                            .map(property -> propertyToRowConverter.apply(ward.getCode(), property))
                            .flatMap(Collection::stream)
                            .collect(toList());
                    return tableBuilder.generateTableRows(electors, mainStreetName, ward.getName(), constituencyName);
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }
}
