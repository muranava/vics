package com.infinityworks.webapp.error;

public class BadRequestFailure extends Exception {
    public BadRequestFailure(String message) {
        super(message);
    }
}
