package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.paf.dto.RecordVote;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.paf.client.PafClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to record that an elector has voted
 */
@Service
public class RecordVoteService {
    private final PafClient pafClient;
    private final WardService wardService;

    @Autowired
    public RecordVoteService(PafClient pafClient, WardService wardService) {
        this.pafClient = pafClient;
        this.wardService = wardService;
    }

    /**
     * Records that a an elector has voted
     *
     * @param permissible the user entering the elector information
     * @param recordVote  the record voter details
     * @return the recorded vote if success, else a failure object
     */
    public Try<RecordVote> recordVote(Permissible permissible, RecordVote recordVote) {
        return permissible
                .ensureWriteAccess()
                .flatMap(user -> wardService.getByCode(recordVote.getWardCode(), permissible)
                .flatMap(ward -> pafClient.recordVoted(recordVote)));
    }
}
