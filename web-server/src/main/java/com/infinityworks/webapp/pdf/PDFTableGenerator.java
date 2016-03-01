package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.model.GeneratedPdfTable;
import com.infinityworks.webapp.paf.dto.Property;

import java.util.List;

public interface PDFTableGenerator {
    List<GeneratedPdfTable> generateTables(List<List<Property>> votersByStreet,
                                           String wardCode,
                                           String wardName,
                                           String constituencyName);
}
