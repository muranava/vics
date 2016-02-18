package com.infinityworks.webapp.service;

import com.infinityworks.pdfgen.DocumentBuilder;
import com.infinityworks.pdfgen.GeneratedPdfTable;
import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.pdf.VotersPdfGenerator;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.client.PafClient;
import com.infinityworks.webapp.service.client.VotersByStreet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

/**
 * Searches for electors within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class ElectorsService {
    private final Logger log = LoggerFactory.getLogger(ElectorsService.class);
    private final PafClient pafClient;
    private final WardService wardService;
    private final VotersPdfGenerator pdfRenderer;
    private final DocumentBuilder documentBuilder;

    @Autowired
    public ElectorsService(PafClient pafClient,
                           WardService wardService,
                           VotersPdfGenerator pdfRenderer,
                           DocumentBuilder documentBuilder) {
        this.pafClient = pafClient;
        this.wardService = wardService;
        this.pdfRenderer = pdfRenderer;
        this.documentBuilder = documentBuilder;
    }

    public Try<List<VotersByStreet>> findElectorsByStreet(TownStreets townStreets, String wardCode, User user) {
        log.debug("Finding electors by streets={} for user={}", townStreets, user);

        Optional<Ward> byCode = wardService.findByCode(wardCode).stream().findFirst();
        if (!byCode.isPresent()) {
            String msg = String.format("No ward with code=%s", wardCode);
            log.warn(msg);
            return Try.failure(new NotFoundFailure(msg));
        }

        if (!user.hasWardPermission(byCode.get())) {
            String msg = String.format("User=%s tried to access ward=%s", user, wardCode);
            log.warn(msg);
            return Try.failure(new NotAuthorizedFailure("Not Authorized"));
        }

        Try<List<VotersByStreet>> electorsByStreet = pafClient.findElectorsByStreet(townStreets, wardCode);
        electorsByStreet.accept(ignored -> {}, electors -> log.debug("Retrieved {} streets", electors.size()));
        return electorsByStreet;
    }

    /**
     * Generates a PDF with the electors grouped by the given streets.
     *
     * @param townStreets the streets to search for electors for
     * @param wardCode    the ward code associated with the streets
     * @param user        the user making the request. Must have permission for the given ward
     * @return the PDF contents as a byte stream
     */
    public Try<ByteArrayOutputStream> electorsByStreets(TownStreets townStreets, String wardCode, User user) {
        log.debug("Finding electors by streets={} for user={}", townStreets, user);

        Optional<Ward> byCode = wardService.findByCode(wardCode).stream().findFirst();
        if (!byCode.isPresent()) {
            String msg = String.format("No ward with code=%s", wardCode);
            log.warn(msg);
            return Try.failure(new NotFoundFailure(msg));
        }

        if (!user.hasWardPermission(byCode.get())) {
            String msg = String.format("User=%s tried to access ward=%s", user, wardCode);
            log.warn(msg);
            return Try.failure(new NotAuthorizedFailure("Not Authorized"));
        }

        log.debug("Generating PDF... ward={} constituency={}", wardCode, byCode.get().getConstituency().getCode());
        Try<ByteArrayOutputStream> pdfContent = pafClient.findElectorsByStreet(townStreets, wardCode)
                .map(electors -> {
                    List<GeneratedPdfTable> generatedPdfTables = pdfRenderer.generatePDF(
                            electors, byCode.get(), byCode.get().getConstituency());
                    return documentBuilder.buildPages(generatedPdfTables);
                });
        log.debug("Finished generating PDF... ward={} constituency={}", wardCode, byCode.get().getConstituency().getCode());
        return pdfContent;
    }

}
