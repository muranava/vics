package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.command.RecordVoteCommandFactory;
import com.infinityworks.webapp.rest.dto.RecordVote;
import org.junit.Before;
import org.junit.Test;

import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static com.infinityworks.webapp.testsupport.builder.downstream.RecordVoteBuilder.recordVote;
import static com.infinityworks.webapp.testsupport.matcher.TryFailureMatcher.isFailure;
import static com.infinityworks.webapp.testsupport.matcher.TrySuccessMatcher.isSuccess;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RecordVoteServiceTest {

    private final ErnShortFormToLongFormConverter ernFormatEnricher = new ErnShortFormToLongFormConverter();
    private RecordVoteService underTest;
    private PafClient pafClient;
    private WardService wardService;

    @Before
    public void setUp() throws Exception {
        pafClient = mock(PafClient.class);
        wardService = mock(WardService.class);
        underTest = new RecordVoteService(wardService, ernFormatEnricher, new RecordVoteCommandFactory(pafClient, 30000));
    }

    @Test
    public void recordsAVote() throws Exception {
        User user = user().withWriteAccess(true).build();
        RecordVote recordVote = recordVote().withErn("PD-123-1").build();
        Ward ward = ward().withWardCode("E05001221").build();
        given(pafClient.recordVoted("E05001221-PD-123-1")).willReturn(Try.success("E05001221-PD-123-1"));
        given(wardService.getByCode(recordVote.getWardCode(), user)).willReturn(Try.success(ward));

        Try<RecordVote> recordVoteResponse = underTest.recordVote(user, recordVote);

        assertThat(recordVoteResponse, isSuccess(equalTo(recordVote)));
    }

    @Test
    public void failsToRecordVoteIfUserDoesNotHaveWardPermission() throws Exception {
        User user = user().withWriteAccess(true).build();
        RecordVote recordVote = recordVote().build();
        given(pafClient.recordVoted(recordVote.getErn())).willReturn(Try.success(recordVote.getErn()));
        given(wardService.getByCode(recordVote.getWardCode(), user)).willReturn(Try.failure(new NotAuthorizedFailure("failure")));

        Try<RecordVote> recordVoteResponse = underTest.recordVote(user, recordVote);

        assertThat(recordVoteResponse, isFailure(instanceOf(NotAuthorizedFailure.class)));
    }

    @Test
    public void failsToRecordVoteIfUserDoesNotHaveWriteAccess() throws Exception {
        User user = user().withWriteAccess(false).build();
        RecordVote recordVote = recordVote().build();

        Try<RecordVote> recordVoteResponse = underTest.recordVote(user, recordVote);

        assertThat(recordVoteResponse, isFailure(instanceOf(NotAuthorizedFailure.class)));
    }
}
