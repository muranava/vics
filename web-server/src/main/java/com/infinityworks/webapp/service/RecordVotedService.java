package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.RecordVoteCommandFactory;
import com.infinityworks.webapp.converter.ErnShortFormToLongFormConverter;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.rest.dto.RecordVoteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to record that a voter has voted
 */
@Service
public class RecordVotedService {
    private final Logger log = LoggerFactory.getLogger(RecordVotedService.class);
    private final WardService wardService;
    private final ErnShortFormToLongFormConverter ernFormatEnricher;
    private final RecordVoteCommandFactory recordVoteCommandFactory;

    @Autowired
    public RecordVotedService(WardService wardService,
                              ErnShortFormToLongFormConverter ernFormatEnricher,
                              RecordVoteCommandFactory recordVoteCommandFactory) {
        this.wardService = wardService;
        this.ernFormatEnricher = ernFormatEnricher;
        this.recordVoteCommandFactory = recordVoteCommandFactory;
    }

    /**
     * Records that a voter has voted
     *
     * @param user the activist entering the elector information
     * @param recordVote  the voter details
     * @return the recorded vote if success, else a failure object
     */
    public Try<RecordVoteRequest> recordVote(User user, RecordVoteRequest recordVote) {
        return user
                .ensureWriteAccess()
                .flatMap(resolvedUser -> wardService.getByCode(recordVote.getWardCode(), user)
                .flatMap(ward -> ernFormatEnricher.apply(ward.getCode(), recordVote.getErn()))
                .flatMap(ern -> recordVoteCommandFactory.create(ern).execute())
                .map(success -> {
                    log.debug("user={} recorded vote for ern={}", user.getId(), recordVote.getErn());
                    return recordVote;
                }));
    }
}
