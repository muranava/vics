package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.commondto.RecordVote;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.service.client.PafClient;
import org.junit.Before;
import org.junit.Test;

import static com.infinityworks.testsupport.builder.RecordVoteBuilder.recordVote;
import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static com.infinityworks.webapp.testsupport.matcher.TryFailureMatcher.isFailure;
import static com.infinityworks.webapp.testsupport.matcher.TrySuccessMatcher.isSuccess;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RecordVoteServiceTest {

    private RecordVoteService underTest;
    private PafClient pafClient;
    private WardService wardService;

    @Before
    public void setUp() throws Exception {
        pafClient = mock(PafClient.class);
        wardService = mock(WardService.class);
        underTest = new RecordVoteService(pafClient, wardService);
    }

    @Test
    public void recordsAVote() throws Exception {
        User user = user().withWriteAccess(true).build();
        RecordVote recordVote = recordVote().build();
        Ward ward = ward().build();
        given(pafClient.recordVoted(recordVote)).willReturn(Try.success(recordVote));
        given(wardService.getByCode(recordVote.getWardCode(), user)).willReturn(Try.success(ward));

        Try<RecordVote> recordVoteResponse = underTest.recordVote(user, recordVote);

        assertThat(recordVoteResponse, isSuccess(equalTo(recordVote)));
    }

    @Test
    public void failsToRecordVoteIfUserDoesNotHaveWardPermission() throws Exception {
        User user = user().withWriteAccess(true).build();
        RecordVote recordVote = recordVote().build();
        given(pafClient.recordVoted(recordVote)).willReturn(Try.success(recordVote));
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