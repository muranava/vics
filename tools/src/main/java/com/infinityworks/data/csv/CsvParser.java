package com.infinityworks.data.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class CsvParser {
    private final String filePath;

    public CsvParser(String filePath) {
        this.filePath = filePath;
    }

    public <T> List<T> parseContent(Function<CSVRecord, T> rowInterpreter) throws IOException {
        ClassPathResource is = new ClassPathResource(filePath);
        Reader in = new InputStreamReader(is.getInputStream());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

        return stream(records.spliterator(), false)
                .skip(1)
                .map(rowInterpreter::apply)
                .collect(toList());
    }
}
