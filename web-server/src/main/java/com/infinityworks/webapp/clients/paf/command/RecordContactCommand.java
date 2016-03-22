package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.RecordContactRequest;
import com.infinityworks.webapp.clients.paf.dto.RecordContactResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class RecordContactCommand extends HystrixCommand<Try<RecordContactResponse>> {
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
    protected Try<RecordContactResponse> run() throws Exception {
        Call<RecordContactResponse> call = pafClient.recordContact(ern, recordContactRequest);
        return requestExecutor.execute(call);
    }
}
