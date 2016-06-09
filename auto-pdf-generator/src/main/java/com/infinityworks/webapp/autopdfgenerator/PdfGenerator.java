package com.infinityworks.webapp.autopdfgenerator;

import com.google.common.io.Files;
import com.infinityworks.webapp.autopdfgenerator.dto.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

/**
 * Script to generate GOTV cards for all electoral wards in the UK (for backup purposes).
 */
public class PdfGenerator {
    private static final Logger log = LoggerFactory.getLogger(PdfGenerator.class);
    private static final int HTTP_REQUEST_TIMEOUT_MS = 60_000;

    private static final Flags GOTV_FILER = ImmutableFlags.builder()
            .withIntentionFrom(4)
            .withIntentionTo(5)
            .withLikelihoodFrom(1)
            .withLikelihoodTo(5)
            .build();

    public static void main(String... args) throws IOException {
        String resultsDir = "gotv_" + LocalDateTime.now().toString();
        Config conf = loadConfiguration();

        PdfClient pdfClient = new PdfClient(
                conf.getString("pdfServerUrl"),
                conf.getString("pafApiUrl"),
                conf.getString("pafApiToken"),
                HTTP_REQUEST_TIMEOUT_MS);

        CsvParser activeWardsCsvParser = new CsvParser(conf.getString("activeWardsCsv"));
        Set<String> wardWhitelist = activeWardsCsvParser.parseContent(ActiveWardsExtractor.INSTANCE);

        CsvParser allWardsCsvParser = new CsvParser(conf.getString("wardsCsv"));
        Set<DistrictRow> districtRows = allWardsCsvParser.parseContent(WardCsvExtractor.INSTANCE);

        districtRows.stream()
                .filter(ward -> !wardWhitelist.isEmpty() && wardWhitelist.contains(ward.wardCode()))
                .forEach(ward -> generateGotvPdfForWard(resultsDir, pdfClient, ward));
    }

    private static void generateGotvPdfForWard(String resultsDir, PdfClient pdfClient, DistrictRow row) {
        try {
            log.info(String.format("Generating GOTV pdf for ward %s (%s)", row.wardName(), row.wardCode()));

            List<Street> streets = pdfClient.streetsByWard(row.wardCode()).response()
                    .stream()
                    .map(StreetConverter.INSTANCE)
                    .collect(toList());

            log.debug(String.format("Retrieved %s streets for ward %s (%s)", streets.size(), row.wardName(), row.wardCode()));

            GeneratePdfRequest pdfRequest = createPdfRequest(row, streets);
            Optional<byte[]> pdfContent = pdfClient.createGotvCard(pdfRequest);
            if (pdfContent.isPresent() && pdfContent.get().length != 0) {
                log.debug("Retrieved voters PDF, content length: " + pdfContent.get().length);
                writePdfFile(row, pdfContent.get(), resultsDir);
            } else {
                log.debug(String.format("No pledges for ward %s (%s)", row.wardName(), row.wardCode()));
            }
        } catch (Exception e) {
            log.error(String.format("Failed to generate PDF for ward %s (%s)", row.wardName(), row.wardCode()), e);
        }
    }

    private static Config loadConfiguration() {
        Config config = ConfigFactory.load();
        log.info("Config: pdfServerUrl=" + config.getString("pdfServerUrl"));
        log.info("Config: pafApiUrl=" + config.getString("pafApiUrl"));
        log.info("Config: pafApiToken=" + config.getString("pafApiToken"));
        return config;
    }

    private static void writePdfFile(DistrictRow row, byte[] pdfContent, String resultsDir) throws IOException {
        String fileName = String.format("%s_%s", row.wardName(), randomUUID().toString().replace("-", "").substring(0, 18));
        String relativePath = String.format("%s/%s/%s.pdf", resultsDir, row.constituencyName(), fileName);
        Files.createParentDirs(new File(relativePath));

        FileOutputStream fos = new FileOutputStream(relativePath);
        fos.write(pdfContent);
        fos.close();
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

enum WardCsvExtractor implements Function<CSVRecord, DistrictRow> {
    INSTANCE;

    @Override
    public DistrictRow apply(CSVRecord record) {
        return ImmutableDistrictRow.builder()
                .withConstituencyCode(record.get(0))
                .withConstituencyName(record.get(1))
                .withWardCode(record.get(2))
                .withWardName(record.get(3))
                .build();
    }
}

enum ActiveWardsExtractor implements Function<CSVRecord, String> {
    INSTANCE;

    @Override
    public String apply(CSVRecord record) {
        return record.get(0);
    }
}

enum StreetConverter implements Function<PafStreetResponse, Street> {
    INSTANCE;

    @Override
    public Street apply(PafStreetResponse pafStreetResponse) {
        return ImmutableStreet.builder()
                .withDependentStreet(pafStreetResponse.dependentStreet())
                .withDependentLocality(pafStreetResponse.dependentLocality())
                .withMainStreet(pafStreetResponse.mainStreet())
                .withPostTown(pafStreetResponse.postTown())
                .build();
    }
}