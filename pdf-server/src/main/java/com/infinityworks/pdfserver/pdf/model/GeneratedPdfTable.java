package com.infinityworks.pdfserver.pdf.model;

import com.lowagie.text.pdf.PdfPTable;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(init = "with*")
public interface GeneratedPdfTable {
    PdfPTable table();
    String street();
    String wardName();
    String wardCode();
    String constituencyName();
}
