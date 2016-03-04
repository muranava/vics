package com.infinityworks.webapp.error;

public class PafApiFailure extends CanvassError {
    public PafApiFailure(String message) {
        super(message);
    }
    public PafApiFailure(String message, Exception e) {
        super(message, e);
    }
}
