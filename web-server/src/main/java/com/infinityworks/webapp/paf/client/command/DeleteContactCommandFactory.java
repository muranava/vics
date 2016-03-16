package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;
import com.infinityworks.webapp.paf.dto.RecordContactRequest;

public class DeleteContactCommandFactory {
    private final int timeoutMsecs;
    private final PafClient pafClient;
    private final PafRequestExecutor requestExecutor;

    public DeleteContactCommandFactory(PafClient pafClient, int timeoutMsecs, PafRequestExecutor requestExecutor) {
        this.timeoutMsecs = timeoutMsecs;
        this.pafClient = pafClient;
        this.requestExecutor = requestExecutor;
    }

    public DeleteContactCommand create(String ern, String contactId) {
        return new DeleteContactCommand(ern, contactId, pafClient, timeoutMsecs, requestExecutor);
    }
}
