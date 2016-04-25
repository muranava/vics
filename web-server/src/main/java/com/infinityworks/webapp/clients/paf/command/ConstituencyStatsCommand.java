package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.ConstituencyStats;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class ConstituencyStatsCommand extends HystrixCommand<Try<ConstituencyStats>> {
    private final String constituencyCode;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public ConstituencyStatsCommand(String constituencyCode,
                                    PafClient pafClient,
                                    int timeout,
                                    PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("GetConstituencyStats"), timeout);
        this.constituencyCode = constituencyCode;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    @Override
    protected Try<ConstituencyStats> run() throws Exception {
        Call<ConstituencyStats> response = pafClient.constituencyStats(constituencyCode);
        return responseHandler.execute(response);
    }
}
