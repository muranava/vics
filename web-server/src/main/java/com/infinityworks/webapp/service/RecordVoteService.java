package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.paf.client.command.RecordVoteCommandFactory;
import com.infinityworks.webapp.rest.dto.RecordVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to record that a voter has voted
 */
@Service
public class RecordVoteService {
    private final WardService wardService;
    private final ErnShortFormToLongFormConverter ernFormatEnricher;
    private final RecordVoteCommandFactory recordVoteCommandFactory;

    @Autowired
    public RecordVoteService(WardService wardService,
                             ErnShortFormToLongFormConverter ernFormatEnricher,
                             RecordVoteCommandFactory recordVoteCommandFactory) {
        this.wardService = wardService;
        this.ernFormatEnricher = ernFormatEnricher;
        this.recordVoteCommandFactory = recordVoteCommandFactory;
    }

    /**
     * Records that a voter has voted
     *
     * @param permissible the activist entering the elector information
     * @param recordVote  the voter details
     * @return the recorded vote if success, else a failure object
     */
    public Try<RecordVote> recordVote(Permissible permissible, RecordVote recordVote) {
        return permissible
                .ensureWriteAccess()
                .flatMap(user -> wardService.getByCode(recordVote.getWardCode(), user)
                .flatMap(ward -> ernFormatEnricher.apply(ward.getCode(), recordVote.getErn()))
                .flatMap(ern -> recordVoteCommandFactory.create(ern).execute())
                .map(success -> recordVote));
    }
}
