package com.infinityworks.webapp.autopdfgenerator;

import com.google.common.io.Resources;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

class CsvParser {
    private final String filePath;

    CsvParser(String filePath) {
        this.filePath = filePath;
    }

    <T> Set<T> parseContent(Function<CSVRecord, T> rowInterpreter) throws IOException {
        URL resource = Resources.getResource(filePath);
        Reader in = new InputStreamReader(resource.openStream());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

        return stream(records.spliterator(), false)
                .map(rowInterpreter)
                .collect(toSet());
    }
}
