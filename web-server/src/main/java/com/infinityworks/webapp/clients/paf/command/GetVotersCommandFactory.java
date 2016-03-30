package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.PafStreetRequest;
import com.infinityworks.webapp.clients.paf.dto.PafStreetResponse;

import java.util.List;

public class GetVotersCommandFactory {
    private final int timeoutMSecs;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public GetVotersCommandFactory(PafClient pafClient, int timeoutMSecs, PafRequestExecutor responseHandler) {
        this.timeoutMSecs = timeoutMSecs;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    public GetVotersCommand create(List<PafStreetRequest> streets, String wardCode) {
        return new GetVotersCommand(streets, wardCode, pafClient, timeoutMSecs, responseHandler);
    }
}
