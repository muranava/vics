package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;

public class GetStreetsCommandFactory {
    private final int timeoutMsecs;
    private final PafRequestExecutor responseHandler;
    private final PafClient pafClient;

    public GetStreetsCommandFactory(PafClient pafClient, int timeoutMsecs, PafRequestExecutor responseHandler) {
        this.pafClient = pafClient;
        this.timeoutMsecs = timeoutMsecs;
        this.responseHandler = responseHandler;
    }

    public GetStreetsCommand create(String wardCode) {
        return new GetStreetsCommand(wardCode, pafClient, timeoutMsecs, responseHandler);
    }
}
