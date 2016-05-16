package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;

public class ConstituencyStatsCommandFactory {
    private final int timeoutMsecs;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public ConstituencyStatsCommandFactory(PafClient pafClient, int timeoutMsecs, PafRequestExecutor responseHandler) {
        this.timeoutMsecs = timeoutMsecs;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    public ConstituencyStatsCommand create(String constituencyCode) {
        return new ConstituencyStatsCommand(constituencyCode, pafClient, timeoutMsecs, responseHandler);
    }
}
