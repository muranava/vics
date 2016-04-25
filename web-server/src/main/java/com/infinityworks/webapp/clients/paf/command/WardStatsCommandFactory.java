package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;

public class WardStatsCommandFactory {
    private final int timeoutMsecs;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public WardStatsCommandFactory(PafClient pafClient, int timeoutMsecs, PafRequestExecutor responseHandler) {
        this.timeoutMsecs = timeoutMsecs;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    public WardStatsCommand create(String wardCode) {
        return new WardStatsCommand(wardCode, pafClient, timeoutMsecs, responseHandler);
    }
}
