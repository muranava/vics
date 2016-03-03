package com.infinityworks.webapp.pdf;

import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import com.infinityworks.webapp.paf.dto.Property;

import java.util.List;

public interface PDFTableGenerator {
    List<GeneratedPdfTable> generateTables(TableBuilder tableBuilder,
                                           List<List<Property>> votersByStreet,
                                           String wardCode,
                                           String wardName,
                                           String constituencyName);
}
