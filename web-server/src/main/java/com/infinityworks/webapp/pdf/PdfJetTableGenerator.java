package com.infinityworks.webapp.pdf;

import com.infinityworks.common.lang.ListExtras;
import com.infinityworks.webapp.converter.PropertyToRowsConverter;
import com.infinityworks.webapp.clients.paf.dto.Property;
import com.infinityworks.webapp.pdf.model.ElectorRow;
import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.infinityworks.common.lang.StringExtras.isNullOrEmpty;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Component
public class PdfJetTableGenerator implements PDFTableGenerator {
    private final PropertyToRowsConverter propertyToRowsConverter;
    private static final int NUM_EMPTY_ROWS = 28;
    private static final List<ElectorRow> EMPTY_ROWS;

    static {
        EMPTY_ROWS = ListExtras.times(() -> ElectorRow.EMPTY, NUM_EMPTY_ROWS);
    }

    @Autowired
    public PdfJetTableGenerator(PropertyToRowsConverter propertyToRowsConverter) {
        this.propertyToRowsConverter = propertyToRowsConverter;
    }

    /**
     * Build a pdf table for each street. Uses a sequential stream to build each street, which (from benchmarking) is faster than
     * a parallelStream since the number of streets is typically small.
     */
    @Override
    public List<GeneratedPdfTable> generateTables(TableBuilder tableBuilder, List<List<Property>> votersByStreets, String wardCode, String wardName, String constituencyName) {
        List<GeneratedPdfTable> pdfTables = votersByStreets.stream()
                .map(street -> createTableFromStreet(tableBuilder, street, wardCode, wardName, constituencyName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        GeneratedPdfTable emptyTable = createEmptyTable(tableBuilder, wardName, wardCode, constituencyName);

        if (ListExtras.isNullOrEmpty(pdfTables)) {
            return pdfTables;
        } else {
            return Stream.concat(pdfTables.stream(),
                                 singletonList(emptyTable).stream()).collect(toList());
        }
    }

    private GeneratedPdfTable createEmptyTable(TableBuilder tableBuilder, String wardName, String wardCode, String constituencyName) {
        return tableBuilder.generateTableRows(EMPTY_ROWS, "", wardName, wardCode, constituencyName).get();
    }

    private Optional<GeneratedPdfTable> createTableFromStreet(TableBuilder tableBuilder, List<Property> properties, String wardCode, String wardName, String constituencyName) {
        List<ElectorRow> electors = properties
                .stream()
                .map(property -> propertyToRowsConverter.apply(wardCode, property))
                .filter(row -> !row.isEmpty())
                .flatMap(Collection::stream)
                .collect(toList());

        String mainStreetName = extractStreetFromFirstProperty(properties);
        return tableBuilder.generateTableRows(electors, mainStreetName, wardName, wardCode, constituencyName);
    }

    private String extractStreetFromFirstProperty(List<Property> properties) {
            return properties.stream()
                    .findFirst()
                    .map(property -> {
                        String street = property.street();
                        String postCode = property.postCode();
                        return isNullOrEmpty(street)
                                ? postCode
                                : street + ", " + postCode;
                    })
                    .orElse("");
    }
}
