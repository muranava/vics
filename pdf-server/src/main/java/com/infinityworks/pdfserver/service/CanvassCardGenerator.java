package com.infinityworks.pdfserver.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.pafclient.PafClient;
import com.infinityworks.pafclient.PafRequestExecutor;
import com.infinityworks.pafclient.dto.PafStreetRequest;
import com.infinityworks.pafclient.dto.Property;
import com.infinityworks.pafclient.dto.PropertyResponse;
import com.infinityworks.pdfserver.controller.dto.GeneratePdfRequest;
import com.infinityworks.pdfserver.controller.dto.RequestInfo;
import com.infinityworks.pdfserver.converter.PafStreetRequestConverter;
import com.infinityworks.pdfserver.error.NotFoundFailure;
import com.infinityworks.pdfserver.pdf.DocumentBuilder;
import com.infinityworks.pdfserver.pdf.PDFTableGenerator;
import com.infinityworks.pdfserver.pdf.TableBuilder;
import com.infinityworks.pdfserver.pdf.model.GeneratedPdfTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Searches for electorsByStreet within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class CanvassCardGenerator {
    private final Logger log = LoggerFactory.getLogger(CanvassCardGenerator.class);

    private final TableBuilder tableBuilder;
    private final DocumentBuilder documentBuilder;
    private final PafClient pafClient;
    private final PDFTableGenerator pdfTableGenerator;
    private final PafRequestExecutor pafRequestExecutor;
    private final PafStreetRequestConverter streetRequestConverter;

    @Autowired
    public CanvassCardGenerator(@Qualifier("canvass") TableBuilder tableBuilder,
                                @Qualifier("canvass") DocumentBuilder documentBuilder,
                                PafClient pafClient,
                                PDFTableGenerator pdfTableGenerator,
                                PafRequestExecutor pafRequestExecutor,
                                PafStreetRequestConverter streetRequestConverter) {
        this.tableBuilder = tableBuilder;
        this.documentBuilder = documentBuilder;
        this.pafClient = pafClient;
        this.pdfTableGenerator = pdfTableGenerator;
        this.pafRequestExecutor = pafRequestExecutor;
        this.streetRequestConverter = streetRequestConverter;
    }

    public Try<ByteArrayOutputStream> generateCanvassCard(GeneratePdfRequest request) {
        String wardCode = request.getInfo().getWardCode();
        return getPropertiesByStreet(request)
                .flatMap(properties -> generatePdf(request, properties.response()))
                .map(pdfContent -> {
                    log.info("Generated canvass card PDF for ward={}({}) numStreets={}",
                            wardCode, request.getInfo().getWardName(), request.getStreets().size());
                    return pdfContent;
                });
    }

    private Try<PropertyResponse> getPropertiesByStreet(GeneratePdfRequest request) {
        List<PafStreetRequest> streets = request.getStreets().stream().map(streetRequestConverter).collect(toList());
        Call<PropertyResponse> response = pafClient.votersByStreets(request.getInfo().getWardCode(), streets);
        return pafRequestExecutor.execute(response);
    }

    private Try<ByteArrayOutputStream> generatePdf(GeneratePdfRequest request,
                                                   List<List<Property>> properties) {
        RequestInfo info = request.getInfo();
        List<GeneratedPdfTable> generatedPdfTables = pdfTableGenerator.generateTables(tableBuilder,
                properties, info.getWardCode(), info.getWardName(), info.getConstituencyName());

        if (generatedPdfTables.isEmpty()) {
            log.info("No voters found for ward={} numStreets={}", info.getWardCode(), request.getStreets().size());
            return Try.failure(new NotFoundFailure("No voters found"));
        } else {
            ByteArrayOutputStream pdfContent = documentBuilder.buildPdfPages(generatedPdfTables, request.getFlags());
            return Try.success(pdfContent);
        }
    }
}
