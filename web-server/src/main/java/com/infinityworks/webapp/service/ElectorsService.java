package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.pdfgen.DocumentBuilder;
import com.infinityworks.pdfgen.model.GeneratedPdfTable;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.pdf.VotersPdfGenerator;
import com.infinityworks.webapp.rest.dto.ElectorSummary;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.client.PafClient;
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

    // FIXME remove canned data
    public Try<ElectorSummary> electorByErn(String ern) {
        return Try.success(new ElectorSummary(ern, "Jon", "Smith", "25 Boswell Drive, CV2 2DH"));
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

        Optional<Ward> byCode = wardService.findByCode(wardCode).stream().findFirst();
        if (!byCode.isPresent()) {
            String msg = String.format("No ward with code=%s", wardCode);
            log.warn(msg);
            return Try.failure(new NotFoundFailure(msg));
        } else {
            Ward ward = byCode.get();
            Constituency constituency = ward.getConstituency();

            if (!permissible.hasWardPermission(ward)) {
                String msg = String.format("User=%s tried to access ward=%s without permission", permissible, wardCode);
                log.warn(msg);
                return Try.failure(new NotAuthorizedFailure("Not Authorized"));
            }

            log.debug("Generating PDF... ward={} constituency={}", wardCode, constituency.getCode());
            Try<ByteArrayOutputStream> pdfContent = pafClient.findElectorsByStreet(townStreets, wardCode)
                    .map(electors -> {
                        List<GeneratedPdfTable> generatedPdfTables = pdfRenderer.generatePDF(
                                electors, ward.getCode(), ward.getName(), constituency.getName());
                        return documentBuilder.buildPages(generatedPdfTables);
                    });
            log.debug("Finished generating PDF... ward={} constituency={}", wardCode, constituency.getCode());
            return pdfContent;
        }
    }

    /**
     * Adds a contact record representing the outcome of a canvassing activity
     *
     * @param user           the logged in user submitted the request
     * @param ern            the ID of the voter contacted
     * @param contactRequest the contact data
     * @return the updated contact data, or a failure object if the operation failed
     */
    public Try<RecordContactRequest> recordContact(User user, String ern, RecordContactRequest contactRequest) {
        if (!user.getWriteAccess()) {
            log.warn("User={} tried to add contact for ern={} but does not have write access", user, ern);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }
        return pafClient.recordContact(ern, contactRequest);
    }
}
