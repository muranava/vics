package com.infinityworks.webapp.pdf;

import com.infinityworks.webapp.converter.PropertyToRowsConverter;
import com.infinityworks.webapp.paf.dto.Property;
import com.infinityworks.webapp.pdf.model.ElectorRow;
import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.infinityworks.common.lang.StringExtras.isNullOrEmpty;
import static java.util.stream.Collectors.toList;

@Component
public class PdfJetTableGenerator implements PDFTableGenerator {
    private final PropertyToRowsConverter propertyToRowsConverter;

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
        return votersByStreets.stream()
                .map(street -> createTableFromStreet(tableBuilder, street, wardCode, wardName, constituencyName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
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
