package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;

import java.util.Map;

public class SearchVotersCommandFactory {
    private final int timeoutMsecs;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public SearchVotersCommandFactory(PafClient pafClient, int timeoutMsecs, PafRequestExecutor responseHandler) {
        this.timeoutMsecs = timeoutMsecs;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    public SearchVotersCommand create(Map<String, String> parameters) {
        return new SearchVotersCommand(parameters, pafClient, timeoutMsecs, responseHandler);
    }
}
