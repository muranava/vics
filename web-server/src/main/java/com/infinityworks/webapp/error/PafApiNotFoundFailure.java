package com.infinityworks.webapp.error;

public class PafApiNotFoundFailure extends CanvassError {
    public PafApiNotFoundFailure(String message) {
        super(message);
    }

    public PafApiNotFoundFailure(String message, String custom) {
        super(message, custom);
    }
}
