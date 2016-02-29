package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import com.infinityworks.webapp.service.client.PafClient;
import com.infinityworks.webapp.testsupport.builder.RecordContactRequestBuilder;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RecordContactServiceTest {

    private RecordContactService underTest;
    private PafClient pafClient;
    private WardService wardService;

    @Before
    public void setUp() throws Exception {
        pafClient = mock(PafClient.class);
        wardService = mock(WardService.class);
    }

    @Test
    public void recordsContact() throws Exception {
        underTest = new RecordContactService(pafClient, wardService);

        Ward earlsdon = ward().withWardCode("E05001221").build();
        RecordContactRequest request = new RecordContactRequestBuilder()
                .withWardCode(earlsdon.getCode()).build();
        String ern = "PD-123-1";
        User user = user()
                .withWriteAccess(true)
                .withWards(newHashSet(earlsdon))
                .build();
        given(wardService.findByCode("E05001221")).willReturn(singletonList(earlsdon));
        given(pafClient.recordContact(ern, request)).willReturn(Try.success(request));

        Try<RecordContactRequest> contact = underTest.recordContact(user, ern, request);

        assertThat(contact.isSuccess(), is(true));
    }
}
