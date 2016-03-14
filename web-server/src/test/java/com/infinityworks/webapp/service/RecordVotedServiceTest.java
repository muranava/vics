package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;
import com.infinityworks.webapp.paf.client.command.RecordVoteCommandFactory;
import com.infinityworks.webapp.paf.dto.ImmutableRecordVotedResponse;
import com.infinityworks.webapp.paf.dto.RecordVotedResponse;
import com.infinityworks.webapp.rest.dto.RecordVote;
import com.infinityworks.webapp.testsupport.mocks.CallStub;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

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

public class RecordVotedServiceTest {

    private final ErnShortFormToLongFormConverter ernFormatEnricher = new ErnShortFormToLongFormConverter();
    private RecordVotedService underTest;
    private PafClient pafClient;
    private WardService wardService;

    @Before
    public void setUp() throws Exception {
        pafClient = mock(PafClient.class);
        wardService = mock(WardService.class);
        underTest = new RecordVotedService(wardService, ernFormatEnricher, new RecordVoteCommandFactory(pafClient, 30000, new PafRequestExecutor(){}));
    }

    @Test
    public void recordsAVote() throws Exception {
        User user = user().withWriteAccess(true).build();
        RecordVote recordVote = recordVote().withErn("PD-123-1").build();
        Ward ward = ward().withWardCode("E05001221").build();
        Call<RecordVotedResponse> call = CallStub.success(ImmutableRecordVotedResponse.builder().withSuccess(true).build());
        given(pafClient.recordVote("E05001221-PD-123-1")).willReturn(call);
        given(wardService.getByCode(recordVote.getWardCode(), user)).willReturn(Try.success(ward));

        Try<RecordVote> recordVoteResponse = underTest.recordVote(user, recordVote);

        assertThat(recordVoteResponse, isSuccess(equalTo(recordVote)));
    }

    @Test
    public void failsToRecordVoteIfUserDoesNotHaveWardPermission() throws Exception {
        User user = user().withWriteAccess(true).build();
        RecordVote recordVote = recordVote().build();
        Call<RecordVotedResponse> call = CallStub.success(ImmutableRecordVotedResponse.builder().withSuccess(true).build());        given(pafClient.recordVote(recordVote.getErn())).willReturn(call);
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
