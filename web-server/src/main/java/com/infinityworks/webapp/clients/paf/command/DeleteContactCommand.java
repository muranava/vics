package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.DeleteContactResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

import java.util.UUID;

public class DeleteContactCommand extends HystrixCommand<Try<DeleteContactResponse>> {
    private final String ern;
    private final UUID contactId;
    private final PafClient pafClient;
    private final PafRequestExecutor requestExecutor;

    public DeleteContactCommand(String ern,
                                UUID contactId,
                                PafClient pafClient,
                                int timeoutMSecs,
                                PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("DeleteContact"), timeoutMSecs);
        this.ern = ern;
        this.contactId = contactId;
        this.pafClient = pafClient;
        this.requestExecutor = responseHandler;
    }

    @Override
    protected Try<DeleteContactResponse> run() throws Exception {
        Call<DeleteContactResponse> call = pafClient.deleteContact(ern, contactId);
        return requestExecutor.execute(call);
    }
}
