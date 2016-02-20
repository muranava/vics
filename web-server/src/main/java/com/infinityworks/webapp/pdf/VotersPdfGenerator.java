package com.infinityworks.webapp.pdf;

import com.infinityworks.commondto.VotersByStreet;
import com.infinityworks.pdfgen.model.GeneratedPdfTable;

import java.util.List;

public interface VotersPdfGenerator {
    List<GeneratedPdfTable> generatePDF(List<VotersByStreet> voters,
                                        String wardCode,
                                        String wardName,
                                        String constituencyName);
}
