package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.PafStreet;
import com.infinityworks.webapp.clients.paf.dto.PropertyResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

import java.util.List;

public class GetVotersCommand extends HystrixCommand<Try<PropertyResponse>> {
    private final List<PafStreet> streets;
    private final String wardCode;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public GetVotersCommand(List<PafStreet> streets,
                            String wardCode,
                            PafClient pafClient,
                            int timeout,
                            PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("GetVoters"), timeout);
        this.streets = streets;
        this.wardCode = wardCode;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    @Override
    protected Try<PropertyResponse> run() throws Exception {
        Call<PropertyResponse> response = pafClient.votersByStreets(wardCode, streets);
        return responseHandler.execute(response);
    }
}
