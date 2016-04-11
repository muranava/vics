package com.infinityworks.webapp.clients.paf.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.Voter;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

import java.util.List;
import java.util.Map;

public class SearchVotersCommand extends HystrixCommand<Try<List<Voter>>> {
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
    protected Try<List<Voter>> run() throws Exception {
        Call<List<Voter>> voterResponseCall = pafClient.voterSearch(parameters);
        return responseHandler.execute(voterResponseCall);
    }
}
