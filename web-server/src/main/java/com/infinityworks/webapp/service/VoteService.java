package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.service.client.PafClient;
import com.infinityworks.webapp.service.client.RecordVoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final Logger log = LoggerFactory.getLogger(VoteService.class);
    private final PafClient pafClient;

    @Autowired
    public VoteService(PafClient pafClient) {
        this.pafClient = pafClient;
    }

    public Try<RecordVoteResponse> recordVote(Permissible permissible, String ern) {
        if (!permissible.hasWriteAccess()) {
            log.debug("User={} tried to record vote for ern={} but does not have write access", permissible, ern);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }
        return pafClient.recordVoted(ern);
    }
}
