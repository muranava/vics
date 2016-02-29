package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.commondto.Voter;
import com.infinityworks.commondto.VotersByStreet;
import com.infinityworks.pdfgen.DocumentBuilder;
import com.infinityworks.pdfgen.model.GeneratedPdfTable;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.pdf.PDFRenderer;
import com.infinityworks.webapp.rest.dto.SearchElectors;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.client.PafClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Searches for electors within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class ElectorsService {
    private final Logger log = LoggerFactory.getLogger(ElectorsService.class);
    private final PafClient pafClient;
    private final WardService wardService;
    private final PDFRenderer pdfRenderer;
    private final DocumentBuilder documentBuilder;

    @Autowired
    public ElectorsService(PafClient pafClient,
                           WardService wardService,
                           PDFRenderer pdfRenderer,
                           DocumentBuilder documentBuilder) {
        this.pafClient = pafClient;
        this.wardService = wardService;
        this.pdfRenderer = pdfRenderer;
        this.documentBuilder = documentBuilder;
    }

    /**
     * Generates a PDF with the electors grouped by the given streets.
     *
     * @param townStreets the streets to search for electors for
     * @param wardCode    the ward code associated with the streets
     * @param permissible the permissible making the request. Must have permission for the given ward
     * @return the PDF contents as a byte stream
     */
    public Try<ByteArrayOutputStream> electorsByStreets(TownStreets townStreets, String wardCode, Permissible permissible) {
        log.debug("Finding electors by streets={} for permissible={}", townStreets, permissible);

        return wardService.getByCode(wardCode, permissible)
                .flatMap(ward -> pafClient.findElectorsByStreet(townStreets, ward.getCode())
                        .flatMap(electors -> getByteArrayOutputStreamTry(townStreets, ward, electors)));
    }

    private Try<ByteArrayOutputStream> getByteArrayOutputStreamTry(TownStreets townStreets, Ward ward, List<VotersByStreet> electors) {
        List<GeneratedPdfTable> generatedPdfTables = pdfRenderer.generatePDF(
                electors, ward.getCode(), ward.getName(), ward.getConstituency().getName());

        if (generatedPdfTables.isEmpty()) {
            log.debug("No voters found for ward={} streets={}", ward, townStreets);
            return Try.failure(new NotFoundFailure("No voters found"));
        } else {
            ByteArrayOutputStream content = documentBuilder.buildPages(generatedPdfTables);
            return Try.success(content);
        }
    }

    /**
     * Searches for electors by attributes.
     *
     * @param permissible    the current user
     * @param searchElectors the search criteria
     * @return a list of voters for the given search criteria
     */
    public Try<List<Voter>> search(Permissible permissible, SearchElectors searchElectors) {
        String wardCode = searchElectors.getWardCode();
        return wardService.getByCode(wardCode, permissible)
                .flatMap(ward -> {
                    if (!permissible.hasWardPermission(ward)) {
                        String msg = String.format("User=%s tried to access ward=%s without permission", permissible, wardCode);
                        log.warn(msg);
                        return Try.failure(new NotAuthorizedFailure("Not Authorized"));
                    }
                    return pafClient.searchElectors(searchElectors);
                });
    }
}
