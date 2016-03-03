package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.converter.RecordContactToPafConverter;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import org.junit.Before;
import org.junit.Test;

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

public class RecordContactServiceTest {

    private RecordContactService underTest;
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
        underTest = new RecordContactService(pafClient, wardService, recordContactToPafConverter);

        Ward earlsdon = ward().withWardCode("E05001221").build();
        RecordContactRequest request = recordContactRequest()
                .withWardCode(earlsdon.getCode()).build();
        User user = user()
                .withWriteAccess(true)
                .withWards(newHashSet(earlsdon))
                .build();
        given(wardService.getByCode("E05001221", user)).willReturn(Try.success(earlsdon));
        com.infinityworks.webapp.paf.dto.RecordContactRequest contactRecord = recordContactToPafConverter.apply(user, request);
        given(pafClient.recordContact("PD-123-4", contactRecord)).willReturn(Try.success(contactRecord));

        Try<RecordContactRequest> contact = underTest.recordContact(user, "PD-123-4", request);

        assertThat(contact.isSuccess(), is(true));
    }

    @Test
    public void recordsContactFailsIfUserDoesNotHaveWriteAccess() throws Exception {
        underTest = new RecordContactService(pafClient, wardService, recordContactToPafConverter);

        Ward earlsdon = ward().withWardCode("E05001221").build();
        RecordContactRequest request = recordContactRequest()
                .withWardCode(earlsdon.getCode()).build();
        String ern = "PD-123-1";
        User user = user()
                .withWriteAccess(false)
                .withWards(newHashSet(earlsdon))
                .build();
        given(wardService.getByCode("E05001221", user)).willReturn(Try.success(earlsdon));
        com.infinityworks.webapp.paf.dto.RecordContactRequest contactRecord = recordContactToPafConverter.apply(user, request);
        given(pafClient.recordContact(ern, contactRecord)).willReturn(Try.success(contactRecord));

        Try<RecordContactRequest> contact = underTest.recordContact(user, ern, request);

        assertThat(contact, isFailure(instanceOf(NotAuthorizedFailure.class)));
    }

    @Test
    public void failsToRecordContactIfUserDoesNotHaveWardPermission() throws Exception {
        underTest = new RecordContactService(pafClient, wardService, recordContactToPafConverter);

        Ward earlsdon = ward().withWardCode("E05001221").build();
        RecordContactRequest request = recordContactRequest()
                .withWardCode(earlsdon.getCode()).build();
        String ern = "PD-123-1";
        User user = user()
                .withWriteAccess(true)
                .build();
        given(wardService.getByCode("E05001221", user)).willReturn(Try.failure(new NotAuthorizedFailure("forbidden")));

        Try<RecordContactRequest> contact = underTest.recordContact(user, ern, request);

        assertThat(contact, isFailure(instanceOf(NotAuthorizedFailure.class)));
    }
}
