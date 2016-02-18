package com.infinityworks.webapp.pdf;

import com.infinityworks.pdfgen.GeneratedPdfTable;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.service.client.VotersByStreet;

import java.util.List;

public interface VotersPdfGenerator {
    List<GeneratedPdfTable> generatePDF(List<VotersByStreet> votersByStreet, Ward ward, Constituency constituency);
}
