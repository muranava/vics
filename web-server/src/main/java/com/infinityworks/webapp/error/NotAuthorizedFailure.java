package com.infinityworks.webapp.error;

public class NotAuthorizedFailure extends Exception {
    public NotAuthorizedFailure(String message) {
        super(message);
    }
}
