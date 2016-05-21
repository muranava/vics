package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.RecordVotedResponse;
import com.infinityworks.webapp.converter.ErnShortFormToLongFormConverter;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.rest.dto.RecordVoteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;

/**
 * Service to record that a voter has voted
 */
@Service
public class RecordVotedService {
    private final Logger log = LoggerFactory.getLogger(RecordVotedService.class);
    private final WardService wardService;
    private final ErnShortFormToLongFormConverter ernFormatEnricher;
    private final PafClient pafClient;
    private final PafRequestExecutor pafRequestExecutor;

    @Autowired
    public RecordVotedService(WardService wardService,
                              ErnShortFormToLongFormConverter ernFormatEnricher,
                              PafClient pafClient,
                              PafRequestExecutor pafRequestExecutor) {
        this.wardService = wardService;
        this.ernFormatEnricher = ernFormatEnricher;
        this.pafClient = pafClient;
        this.pafRequestExecutor = pafRequestExecutor;
    }

    /**
     * Records that a voter has voted
     *
     * @param user the activist entering the elector information
     * @param recordVote  the voter details
     * @return the recorded vote if success, else a failure object
     */
    @Transactional(readOnly = true)
    public Try<RecordVoteRequest> recordVote(User user, RecordVoteRequest recordVote) {
        return user
                .ensureWriteAccess()
                .flatMap(resolvedUser -> wardService.getByCode(recordVote.getWardCode(), user)
                .flatMap(ward -> ernFormatEnricher.apply(ward.getCode(), recordVote.getErn()))
                .flatMap(ern -> {
                    Call<RecordVotedResponse> call = pafClient.recordVote(ern);
                    return pafRequestExecutor.execute(call);
                })
                .map(success -> {
                    log.debug("user={} recorded vote for ern={}", user.getId(), recordVote.getErn());
                    return recordVote;
                }));
    }
}
