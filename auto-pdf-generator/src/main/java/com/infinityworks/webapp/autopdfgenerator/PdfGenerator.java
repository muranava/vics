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
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.UUID.randomUUID;

/**
 * Script to generate GOTV cards for all electoral wards in the UK (for backup purposes).
 */
public class PdfGenerator {
    private static final Logger log = LoggerFactory.getLogger(PdfGenerator.class);
    private static final int HTTP_REQUEST_TIMEOUT_MS = 60_000;

    public static void main(String... args) throws IOException {
        String resultsDir = "gotv_" + LocalDateTime.now().toString();
        Config conf = loadConfiguration();

        PdfClient pdfClient = new PdfClient(
                conf.getString("pdfServerUrl"),
                conf.getString("pafApiUrl"),
                conf.getString("pafApiToken"),
                HTTP_REQUEST_TIMEOUT_MS);

        GotvCardGenerator gotvCardGenerator = new GotvCardGenerator(pdfClient);
        CsvParser allWardsCsvParser = new CsvParser(conf.getString("wardsCsv"));
        Set<DistrictRow> districtRows = allWardsCsvParser.parseContent(WardCsvExtractor.INSTANCE);

        districtRows.stream()
                .forEach(wardCsvRow -> {
                    try {
                        Optional<byte[]> pdfContent = gotvCardGenerator.generate(wardCsvRow);
                        if (pdfContent.isPresent() && pdfContent.get().length != 0) {
                            log.debug("Retrieved voters PDF, content length: " + pdfContent.get().length);
                            writePdfFile(wardCsvRow, pdfContent.get(), resultsDir);
                        } else {
                            log.debug(String.format("No pledges for ward %s (%s)", wardCsvRow.wardName(), wardCsvRow.wardCode()));
                        }
                    } catch (Exception e) {
                        log.error(String.format("Failed to generate PDF for ward %s (%s)", wardCsvRow.wardName(), wardCsvRow.wardCode()), e);
                    }
                });
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
        String relativePath = String.format("%s/%s_%s/%s.pdf", resultsDir, row.constituencyName(), row.constituencyCode(), fileName);
        Files.createParentDirs(new File(relativePath));

        FileOutputStream fos = new FileOutputStream(relativePath);
        fos.write(pdfContent);
        fos.close();
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