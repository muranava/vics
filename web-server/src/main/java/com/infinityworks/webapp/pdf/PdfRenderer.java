package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.ElectorRow;
import com.infinityworks.pdfgen.GeneratedPdfTable;
import com.infinityworks.pdfgen.TableBuilder;
import com.infinityworks.webapp.converter.PropertyToRowConverter;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.service.client.VotersByStreet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
class PdfRenderer implements VotersPdfGenerator {
    private final PropertyToRowConverter propertyToRowConverter;
    private final TableBuilder tableBuilder;

    @Autowired
    public PdfRenderer(PropertyToRowConverter propertyToRowConverter, TableBuilder tableBuilder) {
        this.propertyToRowConverter = propertyToRowConverter;
        this.tableBuilder = tableBuilder;
    }

    /**
     * Build a pdf table for each street. Uses a sequential stream to build each street, which (from benchmarking) is faster than
     * a parallelStream since the number of streets is typically small.
     */
    public List<GeneratedPdfTable> generatePDF(List<VotersByStreet> voters, Ward ward, Constituency constituency) {
        return voters.stream()
                .map(street -> {
                    String mainStreetName = street.getMainStreetName();
                    List<ElectorRow> electors = street.getProperties()
                            .stream()
                            .map(property -> propertyToRowConverter.apply(ward.getCode(), property))
                            .flatMap(Collection::stream)
                            .collect(toList());
                    return tableBuilder.generateTableRows(electors, mainStreetName, ward.getName(), constituency.getName());
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }
}
