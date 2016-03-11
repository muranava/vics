package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.command.GetVotersCommand;
import com.infinityworks.webapp.paf.client.command.GetVotersCommandFactory;
import com.infinityworks.webapp.paf.dto.Property;
import com.infinityworks.webapp.paf.dto.Voter;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.pdf.PDFTableGenerator;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.SearchElectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Searches for electorsByStreet within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class VoterService {
    private final Logger log = LoggerFactory.getLogger(VoterService.class);
    private final PafClient pafClient;
    private final WardService wardService;
    private final PDFTableGenerator pdfTableGenerator;
    private final GetVotersCommandFactory getVotersCommandFactory;

    @Autowired
    public VoterService(PafClient pafClient,
                        WardService wardService,
                        PDFTableGenerator pdfTableGenerator,
                        GetVotersCommandFactory getVotersCommandFactory) {
        this.pafClient = pafClient;
        this.wardService = wardService;
        this.pdfTableGenerator = pdfTableGenerator;
        this.getVotersCommandFactory = getVotersCommandFactory;
    }

    /**
     * Searches for electors by attributes.
     *
     * @param user    the current user
     * @param searchElectors the search criteria
     * @return a list of voters for the given search criteria
     */
    public Try<List<Voter>> search(User user, SearchElectors searchElectors) {
        String wardCode = searchElectors.getWardCode();
        return wardService.getByCode(wardCode, user)
                .flatMap(ward -> {
                    if (!user.hasWardPermission(ward)) {
                        String msg = String.format("User=%s tried to access ward=%s without permission", user, wardCode);
                        log.warn(msg);
                        return Try.failure(new NotAuthorizedFailure("Not Authorized"));
                    }
                    return pafClient.searchElectors(searchElectors);
                });
    }

    /**
     * Generates a PDF with the electorsByStreet grouped by the given streets.
     *
     * @param tableBuilder constructs the table
     * @param request      the streets and filter criteria to search electorsByStreet for
     * @param wardCode     the ward code associated with the streets
     * @param user         the user making the request. Must have permission for the given ward
     * @return the PDF contents as a byte stream
     */
    public Try<ByteArrayOutputStream> getPdfOfElectorsByStreet(TableBuilder tableBuilder,
                                                               DocumentBuilder documentBuilder,
                                                               ElectorsByStreetsRequest request,
                                                               String wardCode,
                                                               User user) {
        return wardService.getByCode(wardCode, user)
                .flatMap(ward -> {
                    log.info("Generate PDF of voters for ward={}. User={}", wardCode, user);
                    GetVotersCommand getVotersCommand = getVotersCommandFactory.create(request.getStreets(), ward.getCode());
                    return getVotersCommand.execute()
                            .flatMap(electorsByStreet -> renderPdfOfElectorsByStreets(
                                    tableBuilder, documentBuilder, request, ward, electorsByStreet.response()));
                });
    }

    private Try<ByteArrayOutputStream> renderPdfOfElectorsByStreets(TableBuilder tableBuilder,
                                                                    DocumentBuilder documentBuilder,
                                                                    ElectorsByStreetsRequest request,
                                                                    Ward ward,
                                                                    List<List<Property>> electors) {
        List<GeneratedPdfTable> generatedPdfTables = pdfTableGenerator.generateTables(tableBuilder,
                electors, ward.getCode(), ward.getName(), ward.getConstituency().getName());

        if (generatedPdfTables.isEmpty()) {
            log.info("No voters found for ward={} streets={}", ward, request);
            return Try.failure(new NotFoundFailure("No voters found"));
        } else {
            ByteArrayOutputStream content = documentBuilder.buildPages(generatedPdfTables, request.getFlags());
            return Try.success(content);
        }
    }
}
