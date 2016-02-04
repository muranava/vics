package com.infinityworks.data;

import com.infinityworks.data.csv.CsvParser;
import com.infinityworks.webapp.domain.ElectorWithAddress;
import com.ninja_squad.dbsetup.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.infinityworks.data.ElectorImporter.Headers.*;
import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;
import static java.util.Arrays.stream;

public class ElectorImporter {
    private static final String BEXLEY_FILE = "bexlyfullregdraft.csv";
    private static final Logger log = LoggerFactory.getLogger(ElectorImporter.class);

    enum Headers {
        WARD_CODE("ward_code"),
        POLLING_DISTRICT("polling_district"),
        ELECTOR_ID("elector_id"),
        ELECTOR_SUFFFIX("elector_suffix"),
        DOB("dob"),
        LAST_NAME("last_name"),
        FIRST_NAME("first_name"),
        HOUSE("house"),
        STREET("street"),
        POSTCODE("Postcode4"),
        ID("id"),
        ERN("ern"),
        TITLE("title"),
        FLAG("flag"),
        UPID("UPID"),
        INITIAL("initial");

        private String column;

        Headers(String column) {
            this.column = column;
        }

        public String getColumn() {
            return column;
        }
    }

    public static void main(String... args) throws IOException {
        DatabaseOperationExecutor setup = new DatabaseOperationExecutor(new DbConfig.Default());

        Stream<Headers> stream = stream(values());
        String[] map = stream.map(header ->  header.column).toArray(String[]::new);

        CsvParser csvParser = new CsvParser(map, BEXLEY_FILE);

        List<ElectorWithAddress> electors = csvParser.parseContent(csvElector -> {
            ElectorWithAddress elector = new ElectorWithAddress();
            elector.setCreated(new Date());
            elector.setModified(new Date());
            elector.setDob(null);
            elector.setPollingDistrict(csvElector.get(POLLING_DISTRICT.column));
            elector.setElectorId(csvElector.get(ELECTOR_ID.column));
            elector.setElectorSuffix(csvElector.get(ELECTOR_SUFFFIX.column));
            elector.setFirstName(csvElector.get(FIRST_NAME.column));
            elector.setLastName(csvElector.get(LAST_NAME.column));
            elector.setErn(csvElector.get(ERN.column));
            elector.setHouse(csvElector.get(HOUSE.column));
            elector.setUpid(csvElector.get(UPID.column));
            elector.setWardCode(csvElector.get(WARD_CODE.column));
            elector.setFlag(csvElector.get(FLAG.column));
            elector.setTitle(csvElector.get(TITLE.column));
            elector.setStreet(csvElector.get(STREET.column));
            elector.setInitial(csvElector.get(INITIAL.column));
            elector.setPostCode(csvElector.get(POSTCODE.column));
            return elector;
        });

        log.info("Started elector with address import...");

        Operation operations = sequenceOf(deleteAllFrom("electors_enriched"), DatabaseOperation.insertElectorsWithAddresses(electors));
        setup.execute(operations);

        log.info("Done");
    }
}
