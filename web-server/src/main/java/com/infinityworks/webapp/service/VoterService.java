package com.infinityworks.webapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.*;
import com.infinityworks.webapp.clients.paf.converter.PafStreetRequestConverter;
import com.infinityworks.webapp.clients.paf.dto.*;
import com.infinityworks.webapp.converter.GetFilteredVotersRequestBuilder;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotFoundFailure;
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

import static com.infinityworks.webapp.config.Config.objectMapper;
import static java.util.stream.Collectors.toList;

/**
 * Searches for electorsByStreet within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class VoterService {
    private final Logger log = LoggerFactory.getLogger(VoterService.class);

    private final WardService wardService;
    private final PDFTableGenerator pdfTableGenerator;
    private final GetVotersCommandFactory getVotersCommandFactory;
    private final GetFilteredVotersCommandFactory getFilteredVotersCommandFactory;
    private final SearchVotersCommandFactory searchVotersCommandFactory;
    private final PafStreetRequestConverter streetToPafConverter;
    private final GetFilteredVotersRequestBuilder gotvRequestBuilder;

    @Autowired
    public VoterService(WardService wardService,
                        PDFTableGenerator pdfTableGenerator,
                        GetVotersCommandFactory getVotersCommandFactory,
                        GetFilteredVotersCommandFactory getFilteredVotersCommandFactory,
                        SearchVotersCommandFactory searchVotersCommandFactory,
                        PafStreetRequestConverter streetToPafConverter,
                        GetFilteredVotersRequestBuilder gotvRequestBuilder) {
        this.wardService = wardService;
        this.pdfTableGenerator = pdfTableGenerator;
        this.getVotersCommandFactory = getVotersCommandFactory;
        this.getFilteredVotersCommandFactory = getFilteredVotersCommandFactory;
        this.searchVotersCommandFactory = searchVotersCommandFactory;
        this.streetToPafConverter = streetToPafConverter;
        this.gotvRequestBuilder = gotvRequestBuilder;
    }

    /**
     * Searches for properties by attributes.
     *
     * @param user           the current user
     * @param searchElectors the search criteria
     * @return a list of voters for the given search criteria
     */
    public Try<List<SearchVoterResponse>> search(User user, SearchElectors searchElectors) {
        log.info("Search voters user={} criteria={}", user, searchElectors);

        SearchVotersCommand searchVotersCommand = searchVotersCommandFactory.create(searchElectors.getParameters());
        return  searchVotersCommand.execute();
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
    @Deprecated //use the filtered request method. To be removed in future commit
    public Try<ByteArrayOutputStream> getPdfOfElectorsByStreet(TableBuilder tableBuilder,
                                                               DocumentBuilder documentBuilder,
                                                               ElectorsByStreetsRequest request,
                                                               String wardCode,
                                                               User user) {
        return wardService.getByCode(wardCode, user)
                .flatMap(ward -> {
                    Try<PropertyResponse> propertiesGroupedByStreet = getPropertiesByStreet(request, ward);
                    return propertiesGroupedByStreet.flatMap(properties -> renderPdfOfElectorsByStreets(
                            tableBuilder, documentBuilder, request, ward, properties.response())).map(pdfContent -> {

                        log.info("User={} Generated PDF of voters for ward={} numStreets={}", user, wardCode, request.getStreets().size());
                        return pdfContent;
                    });
                });
    }

    private Try<PropertyResponse> getPropertiesByStreet(ElectorsByStreetsRequest request, Ward ward) {
        List<PafStreetRequest> pafStreets = request.getStreets()
                .stream()
                .map(streetToPafConverter)
                .collect(toList());
        GetVotersCommand getVotersCommand = getVotersCommandFactory.create(pafStreets, ward.getCode());
        return getVotersCommand.execute();
    }

    public Try<ByteArrayOutputStream> getPdfOfFilteredElectorsByStreet(TableBuilder tableBuilder,
                                                               DocumentBuilder documentBuilder,
                                                               ElectorsByStreetsRequest request,
                                                               String wardCode,
                                                               User user) {
        return wardService.getByCode(wardCode, user)
                .flatMap(ward -> {
                    Try<PropertyResponse> propertiesGroupedByStreet = getFilteredPropertiesByStreet(request, ward, user);
                    return propertiesGroupedByStreet.flatMap(properties -> renderPdfOfElectorsByStreets(
                            tableBuilder, documentBuilder, request, ward, properties.response())).map(pdfContent -> pdfContent);
                });
    }

    private Try<PropertyResponse> getFilteredPropertiesByStreet(ElectorsByStreetsRequest request, Ward ward, User user) {
        GotvVoterRequest voterRequest = gotvRequestBuilder.apply(request);

        log.debug("User={} Requested PDF of filtered voters for ward={} numStreets={} for GOTV. filter={}",
                user, ward.getCode(), request.getStreets().size(), serializeRequest(voterRequest));

        GetFilteredVotersCommand getVotersCommand = getFilteredVotersCommandFactory.create(voterRequest, ward.getCode());
        return getVotersCommand.execute();
    }

    private String serializeRequest(GotvVoterRequest request) {
        try {
            return objectMapper.writeValueAsString(request.filter());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize request");
            return "";
        }
    }

    private Try<ByteArrayOutputStream> renderPdfOfElectorsByStreets(TableBuilder tableBuilder,
                                                                    DocumentBuilder documentBuilder,
                                                                    ElectorsByStreetsRequest request,
                                                                    Ward ward,
                                                                    List<List<Property>> properties) {
        List<GeneratedPdfTable> generatedPdfTables = pdfTableGenerator.generateTables(tableBuilder,
                properties, ward.getCode(), ward.getName(), ward.getConstituency().getName());

        if (generatedPdfTables.isEmpty()) {
            log.info("No voters found for ward={} numStreets={}", ward, request.getStreets().size());
            return Try.failure(new NotFoundFailure("No voters found"));
        } else {
            ByteArrayOutputStream content = documentBuilder.buildPdfPages(generatedPdfTables, request.getFlags());
            return Try.success(content);
        }
    }
}
