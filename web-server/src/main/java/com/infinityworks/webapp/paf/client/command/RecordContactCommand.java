package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;
import com.infinityworks.webapp.paf.dto.RecordContactRequest;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class RecordContactCommand extends HystrixCommand<Try<RecordContactRequest>> {
    private final String ern;
    private final RecordContactRequest recordContactRequest;
    private final PafClient pafClient;
    private final PafRequestExecutor requestExecutor;

    public RecordContactCommand(String ern,
                                RecordContactRequest recordContactRequest,
                                PafClient pafClient,
                                int timeoutMSecs,
                                PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("RecordContact"), timeoutMSecs);
        this.ern = ern;
        this.recordContactRequest = recordContactRequest;
        this.pafClient = pafClient;
        this.requestExecutor = responseHandler;
    }

    @Override
    protected Try<RecordContactRequest> run() throws Exception {
        Call<RecordContactRequest> call = pafClient.recordContact(ern, recordContactRequest);
        return requestExecutor.execute(call);
    }
}
