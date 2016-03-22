package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.SearchVoterResponse;
import com.infinityworks.webapp.clients.paf.dto.VoterResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

import java.util.Map;

public class SearchVotersCommand extends HystrixCommand<Try<SearchVoterResponse>> {
    private final Map<String, String> parameters;
    private final PafClient pafClient;
    private final PafRequestExecutor responseHandler;

    public SearchVotersCommand(Map<String, String> parameters,
                               PafClient pafClient,
                               int timeoutMSecs,
                               PafRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("SearchVoters"), timeoutMSecs);
        this.parameters = parameters;
        this.pafClient = pafClient;
        this.responseHandler = responseHandler;
    }

    @Override
    protected Try<SearchVoterResponse> run() throws Exception {
        Call<SearchVoterResponse> voterResponseCall = pafClient.voterSearch(parameters);
        return responseHandler.execute(voterResponseCall);
    }
}
