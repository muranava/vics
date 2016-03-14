package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;

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
