package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.GotvVoterRequest;
import com.infinityworks.webapp.clients.paf.dto.PropertyResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class GetFilteredVotersCommand extends HystrixCommand<Try<PropertyResponse>> {
    private final GotvVoterRequest request;
    private final String wardCode;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public GetFilteredVotersCommand(GotvVoterRequest request,
                                    String wardCode,
                                    PafClient pafClient,
                                    int timeout,
                                    PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("GetGotvVoters"), timeout);
        this.request = request;
        this.wardCode = wardCode;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    @Override
    protected Try<PropertyResponse> run() throws Exception {
        Call<PropertyResponse> response = pafClient.filteredVotersByStreets(wardCode, request);
        return responseHandler.execute(response);
    }
}
