package com.infinityworks.pdfserver.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.pafclient.PafClient;
import com.infinityworks.pafclient.PafRequestExecutor;
import com.infinityworks.pafclient.dto.PafStreetRequest;
import com.infinityworks.pafclient.dto.PropertyResponse;
import com.infinityworks.pdfserver.controller.dto.GeneratePdfRequest;
import com.infinityworks.pdfserver.converter.PafStreetRequestConverter;
import com.infinityworks.pdfserver.pdf.DocumentBuilder;
import com.infinityworks.pdfserver.pdf.PDFTableGenerator;
import com.infinityworks.pdfserver.pdf.CanvassTableBuilder;
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
public class CanvassCardService extends CanvassCardGeneratorTemplate {
    private final Logger log = LoggerFactory.getLogger(CanvassCardService.class);

    @Autowired
    public CanvassCardService(CanvassTableBuilder tableBuilder,
                              @Qualifier("canvass") DocumentBuilder documentBuilder,
                              PafClient pafClient,
                              PDFTableGenerator pdfTableGenerator,
                              PafRequestExecutor pafRequestExecutor,
                              PafStreetRequestConverter streetRequestConverter) {
        super(tableBuilder, documentBuilder, pafClient, pdfTableGenerator, pafRequestExecutor, streetRequestConverter);
    }

    @Override
    public Try<PropertyResponse> getVoters(GeneratePdfRequest request) {
        log.info("Requesting voters to generate gotv canvass card. info={}, flags={}, numStreets={}",
                request.getInfo(), request.getFlags(), request.getStreets().size());

        List<PafStreetRequest> streets = request.getStreets().stream().map(streetRequestConverter).collect(toList());
        Call<PropertyResponse> response = pafClient.votersByStreets(request.getInfo().getWardCode(), streets);
        return pafRequestExecutor.execute(response);
    }

    public Try<ByteArrayOutputStream> generateCanvassCard(GeneratePdfRequest request) {
        return super.generateCanvassCard(request);
    }
}
