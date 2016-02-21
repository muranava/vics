package com.infinityworks.webapp.error;

class CanvassError extends Exception {
    private final String custom;

    CanvassError(String message) {
        super(message);
        this.custom = "";
    }

    CanvassError(String message, String custom) {
        super(message);
        this.custom = custom;
    }

    public String getCustom() {
        return custom;
    }
}
