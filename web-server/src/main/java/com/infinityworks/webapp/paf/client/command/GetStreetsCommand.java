package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;
import com.infinityworks.webapp.paf.dto.StreetsResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class GetStreetsCommand extends HystrixCommand<Try<StreetsResponse>> {
    private final String wardCode;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public GetStreetsCommand(String wardCode,
                             PafClient pafClient,
                             int timeoutMSecs,
                             PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("GetStreets"), timeoutMSecs);
        this.wardCode = wardCode;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    @Override
    protected Try<StreetsResponse> run() throws Exception {
        Call<StreetsResponse> response = pafClient.streetsByWardCode(wardCode);
        return responseHandler.execute(response);
    }
}
