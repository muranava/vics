package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;

public class RecordVoteCommandFactory {
    private final int timeoutMsecs;
    private final PafClient pafClient;
    private final PafRequestExecutor requestExecutor;

    public RecordVoteCommandFactory(PafClient pafClient, int timeoutMsecs, PafRequestExecutor requestExecutor) {
        this.timeoutMsecs = timeoutMsecs;
        this.pafClient = pafClient;
        this.requestExecutor = requestExecutor;
    }

    public RecordVoteCommand create(String ern) {
        return new RecordVoteCommand(ern, pafClient, timeoutMsecs, requestExecutor);
    }
}
