package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;

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
