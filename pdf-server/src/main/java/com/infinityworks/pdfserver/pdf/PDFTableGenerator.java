package com.infinityworks.pdfserver.pdf;

import com.infinityworks.pafclient.dto.Property;
import com.infinityworks.pdfserver.pdf.model.GeneratedPdfTable;

import java.util.List;

public interface PDFTableGenerator {
    List<GeneratedPdfTable> generateTables(TableBuilderTemplate tableBuilder,
                                           List<List<Property>> votersByStreet,
                                           String wardCode,
                                           String wardName,
                                           String constituencyName);
}
