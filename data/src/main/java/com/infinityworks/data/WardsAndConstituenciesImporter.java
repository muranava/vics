package com.infinityworks.data;

import com.infinityworks.data.csv.CsvParser;
import com.infinityworks.webapp.domain.Ward;
import com.ninja_squad.dbsetup.operation.Operation;

import java.io.IOException;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;

/**
 * Parses wards and constituencies csv file and inserts records into a table
 */
public class WardsAndConstituenciesImporter {
    private static final String[] CSV_HEADERS = {"WD14CD", "WD14NM", "PCON14CD", "PCON14NM", "LAD14CD", "LAD14NM", "EER14CD", "EER14NM"};
    private static final String CSV_FILE = "wards-const.csv";

    public static void main(String... args) throws IOException {
        DatabaseOperationExecutor setup = new DatabaseOperationExecutor(new DbConfig.Default());

        CsvParser csvParser = new CsvParser(CSV_HEADERS, CSV_FILE);

        List<Ward> wards = csvParser.parseContent(record -> {
            Ward ward = new Ward();
            ward.setWardCode(record.get("WD14CD"));
            ward.setWardName(record.get("WD14NM"));
            ward.setConstituencyCode(record.get("PCON14CD"));
            ward.setConstituencyName(record.get("PCON14NM"));
            return ward;
        });

        Operation operations = sequenceOf(deleteAllFrom("wards"), DatabaseOperation.insertWards(wards));
        setup.execute(operations);
    }
}
