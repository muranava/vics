package com.infinityworks.webapp.autopdfgenerator;

import com.infinityworks.webapp.autopdfgenerator.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class GotvCardGenerator {
    private static final Logger log = LoggerFactory.getLogger(GotvCardGenerator.class);
    private final PdfClient pdfClient;

    private static final Flags GOTV_FILER = ImmutableFlags.builder()
            .withIntentionFrom(4)
            .withIntentionTo(5)
            .withLikelihoodFrom(0)
            .withLikelihoodTo(5)
            .build();

    public GotvCardGenerator(PdfClient pdfClient) {
        this.pdfClient = pdfClient;
    }

    public Optional<byte[]> generate(DistrictRow row) throws IOException {
        log.info(String.format("Generating GOTV pdf for ward %s (%s)", row.wardName(), row.wardCode()));

        List<Street> streets = pdfClient.streetsByWard(row.wardCode()).response()
                .stream()
                .map(StreetConverter.INSTANCE)
                .collect(toList());

        log.debug(String.format("Retrieved %s streets for ward %s (%s)", streets.size(), row.wardName(), row.wardCode()));

        GeneratePdfRequest pdfRequest = createPdfRequest(row, streets);
        return pdfClient.createGotvCard(pdfRequest);
    }

    private static GeneratePdfRequest createPdfRequest(DistrictRow record, List<Street> streets) {
        return ImmutableGeneratePdfRequest.builder()
                .withFlags(GOTV_FILER)
                .withInfo(ImmutableRequestInfo.builder()
                        .withWardCode(record.wardCode())
                        .withWardName(record.wardName())
                        .withConstituencyName(record.constituencyName())
                        .build())
                .withStreets(streets)
                .build();
    }
}
