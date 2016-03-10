package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.webapp.paf.client.PafClient;

public class RecordVoteCommandFactory {
    private final int timeoutMsecs;
    private final PafClient pafClient;

    public RecordVoteCommandFactory(PafClient pafClient, int timeoutMsecs) {
        this.timeoutMsecs = timeoutMsecs;
        this.pafClient = pafClient;
    }

    public RecordVoteCommand create(String ern) {
        return new RecordVoteCommand(ern, pafClient, timeoutMsecs);
    }
}
