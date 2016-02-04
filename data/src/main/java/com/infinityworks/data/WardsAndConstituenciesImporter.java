package com.infinityworks.data;

import com.infinityworks.webapp.domain.Ward;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * Parses wards and constituencies csv file and inserts records into a table
 */
public class WardsAndConstituenciesImporter {
    private static final Logger log = LoggerFactory.getLogger(WardsAndConstituenciesImporter.class);
    private static final String[] CSV_HEADERS = {"WD14CD", "WD14NM", "PCON14CD", "PCON14NM", "LAD14CD", "LAD14NM", "EER14CD", "EER14NM"};
    private static final String CSV_FILE = "wards-const.csv";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/canvass";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    static class DataSetup implements DataOperation {
        public void run(List<Ward> wards) {
            log.info("Generating wards table...");

            DriverManagerDestination destination =
                    new DriverManagerDestination(DB_URL, DB_USER, DB_PASSWORD);
            Operation operation = sequenceOf(deleteAllFrom("wards"), insertWards(wards));
            DbSetup dbSetup = new DbSetup(destination, operation);
            dbSetup.launch();

            log.info("Done...");
        }
    }

    public static void main(String... args) throws IOException {
        DataSetup setup = new DataSetup();

        ClassPathResource is = new ClassPathResource(CSV_FILE);
        Reader in = new InputStreamReader(is.getInputStream());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(CSV_HEADERS).parse(in);

        List<Ward> wards = stream(records.spliterator(), false)
                .skip(1)
                .map(record -> {
                    Ward ward = new Ward();
                    ward.setWardCode(record.get("WD14CD"));
                    ward.setWardName(record.get("WD14NM"));
                    ward.setConstituencyCode(record.get("PCON14CD"));
                    ward.setConstituencyName(record.get("PCON14NM"));
                    return ward;
                })
                .collect(toList());

        setup.run(wards);
    }
}
