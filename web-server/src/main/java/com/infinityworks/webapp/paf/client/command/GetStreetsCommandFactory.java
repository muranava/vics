package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.webapp.paf.client.PafClient;

public class GetStreetsCommandFactory {
    private final int timeoutMsecs;
    private final PafClient pafClient;

    public GetStreetsCommandFactory(PafClient pafClient, int timeoutMsecs) {
        this.pafClient = pafClient;
        this.timeoutMsecs = timeoutMsecs;
    }

    public GetStreetsCommand create(String wardCode) {
        return new GetStreetsCommand(wardCode, pafClient, timeoutMsecs);
    }
}
