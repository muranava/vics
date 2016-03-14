package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;
import com.infinityworks.webapp.paf.dto.PafStreet;

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

    public GetVotersCommand create(List<PafStreet> streets, String wardCode) {
        return new GetVotersCommand(streets, wardCode, pafClient, timeoutMSecs, responseHandler);
    }
}
