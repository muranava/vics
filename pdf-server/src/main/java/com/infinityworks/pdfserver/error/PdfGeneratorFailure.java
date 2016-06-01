package com.infinityworks.pdfserver.error;

public class PdfGeneratorFailure extends Exception {
    public PdfGeneratorFailure(String message) {
        super(message);
    }
    public PdfGeneratorFailure(String message, Exception e) {
        super(message, e);
    }
}
