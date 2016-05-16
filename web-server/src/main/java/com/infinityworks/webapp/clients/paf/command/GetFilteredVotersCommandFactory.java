package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.GotvVoterRequest;

public class GetFilteredVotersCommandFactory {
    private final int timeoutMSecs;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public GetFilteredVotersCommandFactory(PafClient pafClient, int timeoutMSecs, PafRequestExecutor responseHandler) {
        this.timeoutMSecs = timeoutMSecs;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    public GetFilteredVotersCommand create(GotvVoterRequest request, String wardCode) {
        return new GetFilteredVotersCommand(request, wardCode, pafClient, timeoutMSecs, responseHandler);
    }
}
