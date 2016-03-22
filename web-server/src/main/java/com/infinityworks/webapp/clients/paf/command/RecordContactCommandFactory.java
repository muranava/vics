package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.RecordContactRequest;

public class RecordContactCommandFactory {
    private final int timeoutMsecs;
    private final PafClient pafClient;
    private final PafRequestExecutor requestExecutor;

    public RecordContactCommandFactory(PafClient pafClient, int timeoutMsecs, PafRequestExecutor requestExecutor) {
        this.timeoutMsecs = timeoutMsecs;
        this.pafClient = pafClient;
        this.requestExecutor = requestExecutor;
    }

    public RecordContactCommand create(String ern, RecordContactRequest contactRequest) {
        return new RecordContactCommand(ern, contactRequest, pafClient, timeoutMsecs, requestExecutor);
    }
}
