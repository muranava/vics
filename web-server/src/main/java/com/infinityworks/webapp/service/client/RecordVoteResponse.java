package com.infinityworks.webapp.service.client;

public class RecordVoteResponse {
    private final String ern;
    private final boolean success;

    public RecordVoteResponse(String ern, boolean success) {
        this.ern = ern;
        this.success = success;
    }

    public String getErn() {
        return ern;
    }

    public boolean isSuccess() {
        return success;
    }
}
