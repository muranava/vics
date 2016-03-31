package com.infinityworks.webapp.testsupport.builder.downstream;

import com.infinityworks.webapp.rest.dto.RecordVoteRequest;

public class RecordVoteBuilder {
    private String wardCode;
    private String wardName;
    private String ern;
    private Boolean success;

    public static RecordVoteBuilder recordVote() {
        return new RecordVoteBuilder().withDefaults();
    }

    public RecordVoteBuilder withDefaults() {
        withWardCode("E05001221")
                .withWardName("Earlsdon")
                .withErn("E05001221-PD-123-4")
                .withSuccess(true);
        return this;
    }

    public RecordVoteBuilder withWardCode(String wardCode) {
        this.wardCode = wardCode;
        return this;
    }

    public RecordVoteBuilder withWardName(String wardName) {
        this.wardName = wardName;
        return this;
    }

    public RecordVoteBuilder withErn(String ern) {
        this.ern = ern;
        return this;
    }

    public RecordVoteBuilder withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public RecordVoteRequest build() {
        return new RecordVoteRequest(wardCode, wardName, ern, success);
    }
}