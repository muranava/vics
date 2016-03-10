package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.rest.dto.Street;

import java.util.List;

public class GetVotersCommandFactory {
    private final int timeoutMSecs;
    private final PafClient pafClient;

    public GetVotersCommandFactory(PafClient pafClient, int timeoutMSecs) {
        this.timeoutMSecs = timeoutMSecs;
        this.pafClient = pafClient;
    }

    public GetVotersCommand create(List<Street> streets, String wardCode) {
        return new GetVotersCommand(streets, wardCode, pafClient, timeoutMSecs);
    }
}
