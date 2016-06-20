package com.infinityworks.webapp.autopdfgenerator;

import com.google.common.collect.Iterables;
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
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.util.UUID.randomUUID;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

/**
 * Script to generate GOTV cards for all electoral wards in the UK (for backup purposes).
 */
public class PdfGenerator {
    private static final Logger log = LoggerFactory.getLogger(PdfGenerator.class);
    private static final int HTTP_REQUEST_TIMEOUT_MS = 60_000;
    private static final String resultsDir = "gotv_" + LocalDateTime.now().toString();

    public static void main(String... args) throws IOException {
        Config conf = loadConfiguration();

        PdfClient pdfClient = new PdfClient(
                conf.getString("pdfServerUrl"),
                conf.getString("pafApiUrl"),
                conf.getString("pafApiToken"),
                HTTP_REQUEST_TIMEOUT_MS);

        GotvCardGenerator gotvCardGenerator = new GotvCardGenerator(pdfClient);
        CsvParser allWardsCsvParser = new CsvParser(conf.getString("wardsCsv"));
        Set<DistrictRow> districtRows = allWardsCsvParser.parseContent(WardCsvExtractor.INSTANCE);

        Iterables.partition(districtRows, conf.getInt("batchSize"))
                .forEach(batch -> {
                    List<CompletableFuture<Optional<PdfRequestResult>>> futures = batch.stream()
                            .map(districtRow -> supplyAsync(() -> generatePdf(gotvCardGenerator, districtRow)))
                            .collect(toList());

                    futures.stream()
                            .map(CompletableFuture::join)
                            .forEach(pdf -> {
                                if (pdf.isPresent()) {
                                    PdfRequestResult pdfResult = pdf.get();
                                    writePdfFile(pdfResult.getMeta(), pdfResult.getContent(), resultsDir);
                                }
                            });
                });
    }

    private static Optional<PdfRequestResult> generatePdf(GotvCardGenerator gotvCardGenerator, DistrictRow wardCsvRow) {
        try {
            Optional<byte[]> pdfContent = gotvCardGenerator.generate(wardCsvRow);
            if (pdfContent.isPresent() && pdfContent.get().length != 0) {
                log.debug("Retrieved voters PDF, content length: " + pdfContent.get().length);
                return Optional.of(new PdfRequestResult(pdfContent.get(), wardCsvRow));
            } else {
                log.debug(String.format("No pledges for ward %s (%s)", wardCsvRow.wardName(), wardCsvRow.wardCode()));
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error(String.format("Failed to generate PDF for ward %s (%s)", wardCsvRow.wardName(), wardCsvRow.wardCode()), e);
            return Optional.empty();
        }
    }

    private static Config loadConfiguration() {
        Config config = ConfigFactory.load();
        log.info("Config: pdfServerUrl=" + config.getString("pdfServerUrl"));
        log.info("Config: pafApiUrl=" + config.getString("pafApiUrl"));
        log.info("Config: pafApiToken=" + config.getString("pafApiToken"));
        return config;
    }

    private static void writePdfFile(DistrictRow row, byte[] pdfContent, String resultsDir) {
        String fileName = String.format("%s_%s", row.wardName(), randomUUID().toString().replace("-", "").substring(0, 18));
        String relativePath = String.format("%s/%s_%s/%s.pdf", resultsDir, row.constituencyName(), row.constituencyCode(), fileName);
        try {
            Files.createParentDirs(new File(relativePath));
            FileOutputStream fos = new FileOutputStream(relativePath);
            fos.write(pdfContent);
            fos.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not write pdf file", e);
        }
    }
}

class PdfRequestResult {
    private final byte[] content;
    private final DistrictRow meta;

    PdfRequestResult(byte[] content, DistrictRow meta) {
        this.content = content;
        this.meta = meta;
    }

    byte[] getContent() {
        return content;
    }

    DistrictRow getMeta() {
        return meta;
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