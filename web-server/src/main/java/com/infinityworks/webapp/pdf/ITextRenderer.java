package com.infinityworks.webapp.pdf;

import com.infinityworks.common.lang.StringExtras;
import com.infinityworks.commondto.VotersByStreet;
import com.infinityworks.pdfgen.TableBuilder;
import com.infinityworks.pdfgen.converter.PropertyToRowConverter;
import com.infinityworks.pdfgen.model.ElectorRow;
import com.infinityworks.pdfgen.model.GeneratedPdfTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Component
public class ITextRenderer implements PDFRenderer {
    private final PropertyToRowConverter propertyToRowConverter;
    private final TableBuilder tableBuilder;

    @Autowired
    public ITextRenderer(PropertyToRowConverter propertyToRowConverter, TableBuilder tableBuilder) {
        this.propertyToRowConverter = propertyToRowConverter;
        this.tableBuilder = tableBuilder;
    }

    /**
     * Build a pdf table for each street. Uses a sequential stream to build each street, which (from benchmarking) is faster than
     * a parallelStream since the number of streets is typically small.
     */
    public List<GeneratedPdfTable> generatePDF(List<VotersByStreet> voters, String wardCode, String wardName, String constituencyName) {
        return voters.stream()
                .map(street -> createTableFromStreet(street, wardCode, wardName, constituencyName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<GeneratedPdfTable> createTableFromStreet(VotersByStreet street, String wardCode, String wardName, String constituencyName) {
        String mainStreetName = asList(street.getMainStreetName(), street.getPostCode())
                .stream()
                .filter(str -> !StringExtras.isNullOrEmpty(str))
                .collect(joining(", "));
        List<ElectorRow> electors = street.getProperties()
                .stream()
                .map(property -> propertyToRowConverter.apply(wardCode, property))
                .flatMap(Collection::stream)
                .collect(toList());
        return tableBuilder.generateTableRows(electors, mainStreetName, wardName, wardCode, constituencyName);
    }
}
