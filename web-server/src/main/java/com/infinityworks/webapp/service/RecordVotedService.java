package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.pafclient.PafClient;
import com.infinityworks.pafclient.PafRequestExecutor;
import com.infinityworks.pafclient.dto.*;
import com.infinityworks.webapp.domain.Ern;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.rest.dto.ImmutableRecordVoteResponse;
import com.infinityworks.webapp.rest.dto.RecordVoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

/**
 * Service to record that a voter has voted
 */
@Service
public class RecordVotedService {
    private final Logger log = LoggerFactory.getLogger(RecordVotedService.class);

    private static final String CONTACT_TYPE = "canvass";

    private final WardService wardService;
    private final PafClient pafClient;
    private final PafRequestExecutor pafRequestExecutor;

    @Autowired
    public RecordVotedService(WardService wardService,
                              PafClient pafClient,
                              PafRequestExecutor pafRequestExecutor) {
        this.wardService = wardService;
        this.pafClient = pafClient;
        this.pafRequestExecutor = pafRequestExecutor;
    }

    /**
     * Records that a voter has voted
     *
     * @param user the activist entering the elector information
     * @param ern  the voter identifier
     * @return the recorded vote if success, else a failure object
     */
    public Try<RecordVoteResponse> recordVote(User user, Ern ern) {
        return user
                .ensureWriteAccess()
                .flatMap(resolvedUser -> wardService.getByCode(ern.getWardCode(), user)
                        .flatMap(ward -> {
                            Call<RecordVotedResponse> call = pafClient.recordVote(ern.longForm());
                            return pafRequestExecutor.execute(call);
                        })
                        .map(success -> {
                            log.debug("user={} recorded vote for ern={}", user, ern.longForm());
                            return ImmutableRecordVoteResponse.builder()
                                    .withErn(ern.longForm())
                                    .withWardCode(ern.getWardCode())
                                    .build();
                        }));
    }

    /**
     * Records that a voter wont vote
     *
     * @param user the activist entering the elector information
     * @param ern  the voter identifier
     * @return the voter info if success, else a failure object
     */
    public Try<RecordVoteResponse> wontVote(User user, Ern ern) {
        return user
                .ensureWriteAccess()
                .flatMap(resolvedUser -> wardService.getByCode(ern.getWardCode(), user)
                        .flatMap(ward -> {
                            RecordContactRequest contactRequest = createWontVoteContactRequest(user, ern);
                            Call<RecordContactResponse> call = pafClient.recordContact(ern.longForm(), contactRequest);
                            return pafRequestExecutor.execute(call);
                        })
                        .map(success -> {
                            log.debug("user={} recorded wont vote for ern={}", user, ern.longForm());
                            return ImmutableRecordVoteResponse.builder()
                                    .withErn(ern.longForm())
                                    .withWardCode(ern.getWardCode())
                                    .build();
                        }));
    }

    private RecordContactRequest createWontVoteContactRequest(User user, Ern ern) {
        return ImmutableRecordContactRequest.builder()
                .withContactType(CONTACT_TYPE)
                .withUserId(user.getId().toString())
                .withVoting(ImmutableVoting.builder()
                        .withIntention(0)
                        .withLikelihood(0)
                        .build())
                .build();
    }

    /**
     * Records the a voter has not voted (effectively performs an undo operation if the vote is recorded)
     *
     * @param user the activist entering the elector information
     * @param ern  the voter ern
     */
    public Try<RecordVoteResponse> undoVote(User user, Ern ern) {
        return user
                .ensureWriteAccess()
                .flatMap(resolvedUser -> wardService.getByCode(ern.getWardCode(), user)
                        .flatMap(wardCode -> {
                            Call<RecordVotedResponse> call = pafClient.undoVote(ern.longForm());
                            return pafRequestExecutor.execute(call);
                        })
                        .map(success -> {
                            log.debug("user={} undo vote for ern={}", user.getId(), ern.longForm());
                            return ImmutableRecordVoteResponse.builder()
                                    .withErn(ern.shortForm())
                                    .withWardCode(ern.getWardCode())
                                    .build();
                        }));
    }
}
