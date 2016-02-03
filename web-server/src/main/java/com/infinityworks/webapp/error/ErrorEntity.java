package com.infinityworks.webapp.error;

public class ErrorEntity {
    private final String type;
    private final String message;

    public ErrorEntity(String type, String message) {
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
