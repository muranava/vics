package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Ern;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafRequestExecutor;
import com.infinityworks.webapp.paf.client.command.DeleteContactCommandFactory;
import com.infinityworks.webapp.paf.client.command.RecordContactCommandFactory;
import com.infinityworks.webapp.paf.converter.RecordContactToPafConverter;
import com.infinityworks.webapp.paf.dto.ImmutableRecordContactResponse;
import com.infinityworks.webapp.paf.dto.RecordContactResponse;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import com.infinityworks.webapp.testsupport.mocks.CallStub;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

import java.util.UUID;

import static com.google.common.collect.Sets.newHashSet;
import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static com.infinityworks.webapp.testsupport.builder.downstream.RecordContactRequestBuilder.recordContactRequest;
import static com.infinityworks.webapp.testsupport.matcher.TryFailureMatcher.isFailure;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ContactServiceTest {

    private ContactService underTest;
    private PafClient pafClient;
    private WardService wardService;
    private RecordContactToPafConverter recordContactToPafConverter = new RecordContactToPafConverter();

    @Before
    public void setUp() throws Exception {
        pafClient = mock(PafClient.class);
        wardService = mock(WardService.class);
    }

    @Test
    public void recordsContact() throws Exception {
        PafRequestExecutor requestExecutor = new PafRequestExecutor() {};
        underTest = new ContactService(wardService, recordContactToPafConverter, new RecordContactCommandFactory(pafClient, 3000, requestExecutor), new DeleteContactCommandFactory(pafClient, 1000, requestExecutor));

        Ward earlsdon = ward().withWardCode("E05001221").build();
        RecordContactRequest request = recordContactRequest().build();
        User user = user()
                .withWriteAccess(true)
                .withWards(newHashSet(earlsdon))
                .build();
        given(wardService.getByCode("E05001221", user)).willReturn(Try.success(earlsdon));
        com.infinityworks.webapp.paf.dto.RecordContactRequest contactRecord = recordContactToPafConverter.apply(user, request);
        Call<RecordContactResponse> success = CallStub.success(ImmutableRecordContactResponse.builder().withContactId(UUID.randomUUID()).withErn("E05001221-PD-123-4").build());
        given(pafClient.recordContact("E05001221-PD-123-4", contactRecord)).willReturn(success);

        Try<RecordContactResponse> contact = underTest.recordContact(user, Ern.valueOf("E05001221-PD-123-4"), request);

        assertThat(contact.isSuccess(), is(true));
    }

    @Test
    public void recordsContactFailsIfUserDoesNotHaveWriteAccess() throws Exception {
        PafRequestExecutor requestExecutor = new PafRequestExecutor() {};
        underTest = new ContactService(wardService, recordContactToPafConverter, new RecordContactCommandFactory(pafClient, 3000, requestExecutor), new DeleteContactCommandFactory(pafClient, 1000, requestExecutor));

        Ward earlsdon = ward().withWardCode("E05001221").build();
        RecordContactRequest request = recordContactRequest().build();
        User user = user()
                .withWriteAccess(false)
                .withWards(newHashSet(earlsdon))
                .build();
        given(wardService.getByCode("E05001221", user)).willReturn(Try.success(earlsdon));

        Try<RecordContactResponse> contact = underTest.recordContact(user, Ern.valueOf("E05001221-PD-123-1"), request);

        assertThat(contact, isFailure(instanceOf(NotAuthorizedFailure.class)));
    }

    @Test
    public void failsToRecordContactIfUserDoesNotHaveWardPermission() throws Exception {
        PafRequestExecutor requestExecutor = new PafRequestExecutor() {};
        underTest = new ContactService(wardService, recordContactToPafConverter, new RecordContactCommandFactory(pafClient, 3000, requestExecutor), new DeleteContactCommandFactory(pafClient, 1000, requestExecutor));

        RecordContactRequest request = recordContactRequest().build();
        User user = user()
                .withWriteAccess(true)
                .build();
        given(wardService.getByCode("E05001221", user)).willReturn(Try.failure(new NotAuthorizedFailure("forbidden")));

        Try<RecordContactResponse> contact = underTest.recordContact(user, Ern.valueOf("E05001221-PD-123-1"), request);

        assertThat(contact, isFailure(instanceOf(NotAuthorizedFailure.class)));
    }
}
