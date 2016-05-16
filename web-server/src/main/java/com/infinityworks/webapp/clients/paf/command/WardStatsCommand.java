package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.WardStats;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class WardStatsCommand extends HystrixCommand<Try<WardStats>> {
    private final String wardCode;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public WardStatsCommand(String wardCode,
                            PafClient pafClient,
                            int timeout,
                            PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("WardStats"), timeout);
        this.wardCode = wardCode;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    @Override
    protected Try<WardStats> run() throws Exception {
        Call<WardStats> response = pafClient.wardStats(wardCode);
        return responseHandler.execute(response);
    }
}
