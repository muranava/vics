package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.service.client.PafClient;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Sets.newHashSet;
import static com.infinityworks.webapp.testsupport.builder.StreetBuilder.street;
import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static com.infinityworks.webapp.testsupport.matcher.TrySuccessMatcher.isSuccess;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AddressServiceTest {

    private AddressService underTest;
    private PafClient pafClient;
    private WardService wardService;

    @Before
    public void setUp() throws Exception {
        pafClient = mock(PafClient.class);
        wardService = mock(WardService.class);
        underTest = new AddressService(wardService, pafClient);
    }

    @Test
    public void returnsNotFoundIfNoWardWhenGettingTownStreetsByWardCode() throws Exception {
        User u = user().build();
        String wardCode = "E0911135";
        given(wardService.getByCode(wardCode, u)).willReturn(Try.failure(new NotFoundFailure("failed")));

        Try<List<Street>> streets = underTest.getTownStreetsByWardCode(wardCode, u);

        assertThat(streets.isSuccess(), is(false));
        assertThat(streets.getFailure(), instanceOf(NotFoundFailure.class));
    }

    @Test
    public void returnsNotAuthorizedIfUserDOesNotHaveWwardPermissionWhenGettingTownStreetsByWardCode() throws Exception {
        User userWithoutWardPermissions = user()
                .withWards(newHashSet())
                .withConstituencies(newHashSet())
                .build();
        String wardCode = "E0911135";
        given(wardService.getByCode(wardCode, userWithoutWardPermissions)).willReturn(Try.failure(new NotAuthorizedFailure("unauthorized")));

        Try<List<Street>> streets = underTest.getTownStreetsByWardCode(wardCode, userWithoutWardPermissions);

        assertThat(streets.isSuccess(), is(false));
        assertThat(streets.getFailure(), instanceOf(NotAuthorizedFailure.class));
    }

    @Test
    public void returnsTheStreets() throws Exception {
        Ward ward = ward().build();
        User user = user()
                .withWards(newHashSet(ward))
                .build();
        given(wardService.getByCode(ward.getCode(), user)).willReturn(Try.success(ward));
        List<Street> streets = singletonList(street().build());
        given(pafClient.findStreetsByWardCode(ward.getCode())).willReturn(Try.success(streets));

        Try<List<Street>> streetsResponse = underTest.getTownStreetsByWardCode(ward.getCode(), user);

        assertThat(streetsResponse, isSuccess(equalTo(streets)));
    }

}