package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.service.client.PafClient;
import com.infinityworks.webapp.service.client.RecordVoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private final PafClient pafClient;

    @Autowired
    public VoteService(PafClient pafClient) {
        this.pafClient = pafClient;
    }

    public Try<RecordVoteResponse> recordVote(String ern) {
        return pafClient.recordVoted(ern);
    }
}
