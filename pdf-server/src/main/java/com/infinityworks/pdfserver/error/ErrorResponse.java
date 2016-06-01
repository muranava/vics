package com.infinityworks.pdfserver.error;

class ErrorResponse {
    private final String type;
    private final String message;

    ErrorResponse(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}
