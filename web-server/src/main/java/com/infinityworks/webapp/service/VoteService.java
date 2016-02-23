package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.service.client.PafClient;
import com.infinityworks.webapp.service.client.RecordVote;
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

    public Try<RecordVote> recordVote(Permissible permissible, RecordVote recordVote) {
        if (!permissible.hasWriteAccess()) {
            log.debug("User={} tried to record vote for recordVote={} but does not have write access", permissible, recordVote);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }
        return pafClient.recordVoted(recordVote);
    }
}
