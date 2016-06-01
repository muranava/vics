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
import com.infinityworks.pdfserver.pdf.TableBuilder;
import com.infinityworks.pdfserver.pdf.model.GeneratedPdfTable;

import java.io.ByteArrayOutputStream;
import java.util.List;

abstract class CanvassCardGeneratorTemplate {
    private final TableBuilder tableBuilder;
    private final DocumentBuilder documentBuilder;
    private final PDFTableGenerator pdfTableGenerator;

    final PafClient pafClient;
    final PafRequestExecutor pafRequestExecutor;
    final PafStreetRequestConverter streetRequestConverter;

    CanvassCardGeneratorTemplate(TableBuilder tableBuilder,
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
        Try<PropertyResponse> voters = getVoters(request);
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
            ByteArrayOutputStream pdfContent = documentBuilder.buildPdfPages(generatedPdfTables, request.getFlags());
            return Try.success(pdfContent);
        }
    }
}
