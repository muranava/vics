package com.infinityworks.pdfserver.pdf.renderer;

import com.infinityworks.pdfserver.pdf.model.ElectorRow;
import com.infinityworks.pdfserver.pdf.model.GeneratedPdfTable;

import java.util.List;
import java.util.Optional;

public interface PdfTableGenerator {
    Optional<GeneratedPdfTable> generateTableRows(
            List<ElectorRow> rows, String mainStreetName, String wardName, String wardCode, String constituencyName);
}