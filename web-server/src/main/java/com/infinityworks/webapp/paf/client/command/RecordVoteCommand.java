package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;
import com.infinityworks.webapp.paf.dto.RecordVotedResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class RecordVoteCommand extends HystrixCommand<Try<RecordVotedResponse>> {
    private final String ern;
    private final PafClient pafClient;
    private final PafRequestExecutor requestExecutor;

    public RecordVoteCommand(String ern, PafClient pafClient, int timeoutMSecs, PafRequestExecutor requestExecutor) {
        super(HystrixCommandGroupKey.Factory.asKey("RecordVoted"), timeoutMSecs);
        this.ern = ern;
        this.pafClient = pafClient;
        this.requestExecutor = requestExecutor;
    }

    @Override
    protected Try<RecordVotedResponse> run() throws Exception {
        Call<RecordVotedResponse> call = pafClient.recordVote(ern);
        return requestExecutor.execute(call);
    }
}
