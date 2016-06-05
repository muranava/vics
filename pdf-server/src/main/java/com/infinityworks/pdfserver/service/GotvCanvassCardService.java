package com.infinityworks.pdfserver.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.pafclient.PafClient;
import com.infinityworks.pafclient.PafRequestExecutor;
import com.infinityworks.pafclient.dto.GotvVoterRequest;
import com.infinityworks.pafclient.dto.PropertyResponse;
import com.infinityworks.pdfserver.controller.dto.GeneratePdfRequest;
import com.infinityworks.pdfserver.converter.GotvRequestAdapter;
import com.infinityworks.pdfserver.converter.PafStreetRequestConverter;
import com.infinityworks.pdfserver.pdf.DocumentBuilder;
import com.infinityworks.pdfserver.pdf.GotvTableBuilder;
import com.infinityworks.pdfserver.pdf.PDFTableGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.ByteArrayOutputStream;

/**
 * Searches for electorsByStreet within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class GotvCanvassCardService extends CanvassCardGeneratorTemplate {
    private final Logger log = LoggerFactory.getLogger(GotvCanvassCardService.class);
    private final GotvRequestAdapter gotvRequestAdapter;

    @Autowired
    public GotvCanvassCardService(GotvTableBuilder tableBuilder,
                                  @Qualifier("gotv") DocumentBuilder documentBuilder,
                                  PafClient pafClient,
                                  PDFTableGenerator pdfTableGenerator,
                                  PafRequestExecutor pafRequestExecutor,
                                  PafStreetRequestConverter streetRequestConverter,
                                  GotvRequestAdapter gotvRequestAdapter) {
        super(tableBuilder, documentBuilder, pafClient, pdfTableGenerator, pafRequestExecutor, streetRequestConverter);
        this.gotvRequestAdapter = gotvRequestAdapter;
    }

    @Override
    public Try<PropertyResponse> getVoters(GeneratePdfRequest request) {
        log.info("Requesting voters to generate gotv canvass card. info={}, flags={}, numStreets={}",
                request.getInfo(), request.getFlags(), request.getStreets().size());

        GotvVoterRequest voterRequest = gotvRequestAdapter.apply(request);
        Call<PropertyResponse> response = pafClient.filteredVotersByStreets(request.getInfo().getWardCode(), voterRequest);
        return pafRequestExecutor.execute(response);
    }

    public Try<ByteArrayOutputStream> generateCanvassCard(GeneratePdfRequest request) {
        return super.generateCanvassCard(request);
    }
}
