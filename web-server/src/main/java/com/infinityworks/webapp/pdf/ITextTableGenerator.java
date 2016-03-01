package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.TableBuilder;
import com.infinityworks.pdfgen.model.ElectorRow;
import com.infinityworks.pdfgen.model.GeneratedPdfTable;
import com.infinityworks.webapp.converter.PropertyToRowConverter;
import com.infinityworks.webapp.paf.dto.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.infinityworks.common.lang.StringExtras.isNullOrEmpty;
import static java.util.stream.Collectors.toList;

@Component
public class ITextTableGenerator implements PDFTableGenerator {
    private final PropertyToRowConverter propertyToRowConverter;
    private final TableBuilder tableBuilder;

    @Autowired
    public ITextTableGenerator(PropertyToRowConverter propertyToRowConverter, TableBuilder tableBuilder) {
        this.propertyToRowConverter = propertyToRowConverter;
        this.tableBuilder = tableBuilder;
    }

    /**
     * Build a pdf table for each street. Uses a sequential stream to build each street, which (from benchmarking) is faster than
     * a parallelStream since the number of streets is typically small.
     */
    public List<GeneratedPdfTable> generateTables(List<List<Property>> votersByStreets, String wardCode, String wardName, String constituencyName) {
        return votersByStreets.stream()
                .map(street -> createTableFromStreet(street, wardCode, wardName, constituencyName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<GeneratedPdfTable> createTableFromStreet(List<Property> properties, String wardCode, String wardName, String constituencyName) {
        List<ElectorRow> electors = properties
                .stream()
                .map(property -> propertyToRowConverter.apply(wardCode, property))
                .flatMap(Collection::stream)
                .collect(toList());

        String mainStreetName = extractStreetFromFirstProperty(properties);
        return tableBuilder.generateTableRows(electors, mainStreetName, wardName, wardCode, constituencyName);
    }

    private String extractStreetFromFirstProperty(List<Property> properties) {
            return properties.stream()
                    .findFirst()
                    .map(property -> {
                        String street = property.getStreet();
                        String postCode = property.getPostCode();
                        return isNullOrEmpty(street)
                                ? postCode
                                : street + ", " + postCode;
                    })
                    .orElse("");
    }
}
