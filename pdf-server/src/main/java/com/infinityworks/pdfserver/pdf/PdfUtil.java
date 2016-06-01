package com.infinityworks.pdfserver.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;

import java.io.ByteArrayOutputStream;
import java.util.List;

class PdfUtil {
    static ByteArrayOutputStream mergePdfs(List<byte[]> pdfs) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfCopy copy = new PdfCopy(document, os);
        document.open();

        for (byte[] data : pdfs) {
            PdfReader pdfReader = new PdfReader(data);
            int numPages = pdfReader.getNumberOfPages();
            for (int page = 0; page < numPages;) {
                copy.addPage(copy.getImportedPage(pdfReader, ++page));
            }
            copy.freeReader(pdfReader);
            pdfReader.close();
        }
        document.close();
        return os;
    }
}
