package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommandFactory;
import com.infinityworks.webapp.converter.PafToStreetResponseConverter;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.rest.dto.StreetsByWardResponse;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AddressServiceTest {

    private AddressService underTest;
    private WardService wardService;

    @Before
    public void setUp() throws Exception {
        PafClient pafClient = mock(PafClient.class);
        wardService = mock(WardService.class);
        PafAddressService pafAddressService = new PafAddressService(new GetStreetsCommandFactory(pafClient, 30000, new PafRequestExecutor() {
        }), new PafToStreetResponseConverter());
        underTest = new AddressService(wardService, pafAddressService);
    }

    @Test
    public void returnsNotFoundIfNoWardWhenGettingTownStreetsByWardCode() throws Exception {
        User u = user().build();
        String wardCode = "E0911135";
        given(wardService.getByCode(wardCode, u)).willReturn(Try.failure(new NotFoundFailure("failed")));

        Try<StreetsByWardResponse> streets = underTest.getTownStreetsByWardCode(wardCode, u);

        assertThat(streets.isSuccess(), is(false));
        assertThat(streets.getFailure(), instanceOf(NotFoundFailure.class));
    }

    @Test
    public void returnsNotAuthorizedIfUserDoesNotHaveWwardPermissionWhenGettingTownStreetsByWardCode() throws Exception {
        User userWithoutWardPermissions = user()
                .withWards(newHashSet())
                .withConstituencies(newHashSet())
                .build();
        String wardCode = "E0911135";
        given(wardService.getByCode(wardCode, userWithoutWardPermissions)).willReturn(Try.failure(new NotAuthorizedFailure("unauthorized")));

        Try<StreetsByWardResponse> streets = underTest.getTownStreetsByWardCode(wardCode, userWithoutWardPermissions);

        assertThat(streets.isSuccess(), is(false));
        assertThat(streets.getFailure(), instanceOf(NotAuthorizedFailure.class));
    }

}
