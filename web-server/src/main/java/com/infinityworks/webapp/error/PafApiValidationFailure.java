package com.infinityworks.webapp.error;

public class PafApiValidationFailure extends CanvassError {
    public PafApiValidationFailure(String message) {
        super(message);
    }

    public PafApiValidationFailure(String message, String custom) {
        super(message, custom);
    }
}
