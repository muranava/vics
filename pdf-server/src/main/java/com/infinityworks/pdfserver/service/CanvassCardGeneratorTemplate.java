package com.infinityworks.pdfserver.service;


import com.infinityworks.common.lang.Try;
import com.infinityworks.pafclient.PafClient;
import com.infinityworks.pafclient.PafRequestExecutor;
import com.infinityworks.pafclient.dto.Property;
import com.infinityworks.pafclient.dto.PropertyResponse;
import com.infinityworks.pdfserver.controller.dto.GeneratePdfRequest;
import com.infinityworks.pdfserver.controller.dto.RequestInfo;
import com.infinityworks.pdfserver.converter.PafStreetRequestConverter;
import com.infinityworks.pdfserver.error.NotFoundFailure;
import com.infinityworks.pdfserver.pdf.DocumentBuilder;
import com.infinityworks.pdfserver.pdf.PDFTableGenerator;
import com.infinityworks.pdfserver.pdf.TableBuilderTemplate;
import com.infinityworks.pdfserver.pdf.model.GeneratedPdfTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

abstract class CanvassCardGeneratorTemplate {
    private final TableBuilderTemplate tableBuilder;
    private final DocumentBuilder documentBuilder;
    private final PDFTableGenerator pdfTableGenerator;
    private static final Logger log = LoggerFactory.getLogger(CanvassCardGeneratorTemplate.class);

    final PafClient pafClient;
    final PafRequestExecutor pafRequestExecutor;
    final PafStreetRequestConverter streetRequestConverter;

    CanvassCardGeneratorTemplate(TableBuilderTemplate tableBuilder,
                                 DocumentBuilder documentBuilder,
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

    protected Try<ByteArrayOutputStream> generateCanvassCard(GeneratePdfRequest request) {
        UUID correlationKey = UUID.randomUUID();
        long startTime = System.currentTimeMillis();
        log.info("Paf Request[{}] getVotersByStreets", correlationKey);

        Try<PropertyResponse> voters = getVoters(request);

        long endTime = System.currentTimeMillis();
        log.info("Paf Response[{}] getVotersByStreets. paf_response_time={}", correlationKey, endTime - startTime);

        return voters.flatMap(properties -> generatePdf(request, properties.response()));
    }

    protected abstract Try<PropertyResponse> getVoters(GeneratePdfRequest request);

    private Try<ByteArrayOutputStream> generatePdf(GeneratePdfRequest request,
                                                   List<List<Property>> properties) {
        RequestInfo info = request.getInfo();
        List<GeneratedPdfTable> generatedPdfTables = pdfTableGenerator.generateTables(tableBuilder,
                properties, info.getWardCode(), info.getWardName(), info.getConstituencyName());

        if (generatedPdfTables.isEmpty()) {
            return Try.failure(new NotFoundFailure("No voters found"));
        } else {
            long startTime = System.currentTimeMillis();
            ByteArrayOutputStream pdfContent = documentBuilder.buildPdfPages(generatedPdfTables, request.getFlags());
            long endTime = System.currentTimeMillis();
            log.info("Generated canvass card for ward={} numStreets={} pdf_rendering_time={}",
                    request.getInfo().getWardCode(), request.getStreets().size(), endTime - startTime);
            return Try.success(pdfContent);
        }
    }
}
