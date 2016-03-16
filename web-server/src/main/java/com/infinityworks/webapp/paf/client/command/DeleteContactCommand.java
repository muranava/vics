package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;
import com.infinityworks.webapp.paf.dto.Nil;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class DeleteContactCommand extends HystrixCommand<Try<Nil>> {
    private final String ern;
    private final String contactId;
    private final PafClient pafClient;
    private final PafRequestExecutor requestExecutor;

    public DeleteContactCommand(String ern,
                                String contactId,
                                PafClient pafClient,
                                int timeoutMSecs,
                                PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("RecordContact"), timeoutMSecs);
        this.ern = ern;
        this.contactId = contactId;
        this.pafClient = pafClient;
        this.requestExecutor = responseHandler;
    }

    @Override
    protected Try<Nil> run() throws Exception {
        Call<Nil> call = pafClient.deleteContact(ern, contactId);
        return requestExecutor.execute(call);
    }
}
