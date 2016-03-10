package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.paf.client.PafClient;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class RecordVoteCommand extends HystrixCommand<Try<String>> {
    private final String ern;
    private final PafClient pafClient;

    public RecordVoteCommand(String ern, PafClient pafClient, int timeoutMSecs) {
        super(HystrixCommandGroupKey.Factory.asKey("RecordVote"), timeoutMSecs);
        this.ern = ern;
        this.pafClient = pafClient;
    }

    @Override
    protected Try<String> run() throws Exception {
        return pafClient.recordVoted(ern);
    }
}
