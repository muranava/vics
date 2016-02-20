package com.infinityworks.tools.dataprep;

import org.apache.commons.csv.CSVRecord;

import java.util.function.Function;

public class WardExtractor implements Function<CSVRecord, WardCsvRecord> {
    @Override
    public WardCsvRecord apply(CSVRecord record) {
        WardCsvRecord ward = new WardCsvRecord();
        ward.setWardCode(record.get(0));
        ward.setWardName(record.get(1));
        ward.setConstituencyCode(record.get(2));
        ward.setConstituencyName(record.get(3));
        return ward;
    }
}
