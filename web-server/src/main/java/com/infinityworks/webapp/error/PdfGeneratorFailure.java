package com.infinityworks.webapp.error;

public class PdfGeneratorFailure extends CanvassError {
    public PdfGeneratorFailure(String message) {
        super(message);
    }
    public PdfGeneratorFailure(String message, Exception e) {
        super(message, e);
    }
}
