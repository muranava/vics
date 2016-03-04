package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.error.PafApiNotFoundFailure;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.rest.dto.RecordVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to record that a voter has voted
 */
@Service
public class RecordVoteService {
    private final PafClient pafClient;
    private final WardService wardService;
    private final ErnFormatEnricher ernFormatEnricher;

    @Autowired
    public RecordVoteService(PafClient pafClient,
                             WardService wardService,
                             ErnFormatEnricher ernFormatEnricher) {
        this.pafClient = pafClient;
        this.wardService = wardService;
        this.ernFormatEnricher = ernFormatEnricher;
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
                .flatMap(pafClient::recordVoted))
                .map(success -> recordVote);
    }
}
