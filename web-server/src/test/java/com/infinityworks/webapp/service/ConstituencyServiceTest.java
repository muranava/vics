package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Role;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.rest.dto.UserRestrictedConstituencies;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;
import static com.infinityworks.webapp.testsupport.builder.ConstituencyBuilder.constituency;
import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Collections.emptySet;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ConstituencyServiceTest {

    private ConstituencyRepository constituencyRepository;
    private ConstituencyService underTest;
    private UserRepository userRepository;
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        constituencyRepository = mock(ConstituencyRepository.class);
        userRepository = mock(UserRepository.class);
        userService = mock(UserService.class);
        underTest = new ConstituencyService(constituencyRepository, userRepository, userService);
    }

    @Test
    public void returnsTheRestrictedConstituenciesForUser() throws Exception {

        Constituency rugby = constituency().withName("Rugby").build();
        Constituency covSouth = constituency().withName("Coventry South").build();
        Ward henley = ward().withWardName("Henley").withConstituency(covSouth).build();
        User user = user()
                .withRole(Role.USER)
                .withConstituencies(newHashSet(rugby))
                .withWards(newHashSet(henley))
                .build();

        UserRestrictedConstituencies constituencies = underTest.getVisibleConstituenciesByUserWithWardContext(user);

        assertThat(constituencies.getConstituencies(), hasItem(covSouth));
        assertThat(constituencies.getConstituencies(), hasItem(rugby));
        verifyZeroInteractions(constituencyRepository);
    }

    @Test
    public void associatesConstituencyToUser() throws Exception {
        User admin = user().withRole(Role.ADMIN).build();
        User user = user().withConstituencies(new HashSet<>()).build();
        Constituency constituency = constituency().build();
        given(constituencyRepository.findOne(constituency.getId())).willReturn(constituency);
        given(userRepository.findOne(user.getId())).willReturn(user);
        given(userRepository.save(user)).willReturn(user);

        Try<User> userTry = underTest.associateToUser(admin, constituency.getId(), user.getId());

        assertThat(userTry.isSuccess(), is(true));
        assertThat(userTry.get().getConstituencies(), hasItem(constituency));
    }

    @Test
    public void returnsNotFoundIfUserNotFoundWhenAssociateConstituencyToUser() throws Exception {
        User admin = user().withRole(Role.ADMIN).build();
        User user = user().withConstituencies(new HashSet<>()).build();
        Constituency constituency = constituency().build();
        given(constituencyRepository.findOne(constituency.getId())).willReturn(constituency);
        given(userRepository.findOne(user.getId())).willReturn(null);

        Try<User> userTry = underTest.associateToUser(admin, constituency.getId(), user.getId());

        assertThat(userTry.isSuccess(), is(false));
        assertThat(userTry.getFailure(), is(instanceOf(NotFoundFailure.class)));
    }

    @Test
    public void returnsNotFoundIfConstituencyNotFoundWhenAssociateConstituencyToUser() throws Exception {
        User admin = user().withRole(Role.ADMIN).build();
        User user = user().withConstituencies(new HashSet<>()).build();
        Constituency constituency = constituency().build();
        given(constituencyRepository.findOne(constituency.getId())).willReturn(null);
        given(userRepository.findOne(user.getId())).willReturn(user);

        Try<User> userTry = underTest.associateToUser(admin, constituency.getId(), user.getId());

        assertThat(userTry.isSuccess(), is(false));
        assertThat(userTry.getFailure(), is(instanceOf(NotFoundFailure.class)));
    }

    @Test
    public void returnsNotAuthorizedIfNonAdminWhenAssociateConstituencyToUser() throws Exception {
        User admin = user().withRole(Role.USER).build();
        User user = user().withConstituencies(new HashSet<>()).build();
        Constituency constituency = constituency().build();
        given(constituencyRepository.findOne(constituency.getId())).willReturn(constituency);
        given(userRepository.findOne(user.getId())).willReturn(user);

        Try<User> userTry = underTest.associateToUser(admin, constituency.getId(), user.getId());

        assertThat(userTry.isSuccess(), is(false));
        assertThat(userTry.getFailure(), is(instanceOf(NotAuthorizedFailure.class)));
    }

    @Test
    public void removesAssociationOfConstituencyToUser() throws Exception {
        User admin = user().withRole(Role.ADMIN).build();
        Constituency constituency = constituency().build();
        User user = user().withConstituencies(newHashSet(constituency)).build();
        given(constituencyRepository.findOne(constituency.getId())).willReturn(constituency);
        given(userRepository.findOne(user.getId())).willReturn(user);
        given(userRepository.save(user)).willReturn(user);

        Try<User> userTry = underTest.removeUserAssociation(admin, constituency.getId(), user.getId());

        assertThat(userTry.isSuccess(), is(true));
        assertThat(userTry.get().getConstituencies(), is(emptySet()));
    }


    @Test
    public void returnsNotFoundIfUserNotFoundWhenRemoveConstituencyFromUser() throws Exception {
        User admin = user().withRole(Role.ADMIN).build();
        User user = user().withConstituencies(new HashSet<>()).build();
        Constituency constituency = constituency().build();
        given(constituencyRepository.findOne(constituency.getId())).willReturn(constituency);
        given(userRepository.findOne(user.getId())).willReturn(null);

        Try<User> userTry = underTest.removeUserAssociation(admin, constituency.getId(), user.getId());

        assertThat(userTry.isSuccess(), is(false));
        assertThat(userTry.getFailure(), is(instanceOf(NotFoundFailure.class)));
    }

    @Test
    public void returnsNotFoundIfConstituencyNotFoundWhenRemoveConstituencyFromUser() throws Exception {
        User admin = user().withRole(Role.ADMIN).build();
        User user = user().withConstituencies(new HashSet<>()).build();
        Constituency constituency = constituency().build();
        given(constituencyRepository.findOne(constituency.getId())).willReturn(null);
        given(userRepository.findOne(user.getId())).willReturn(user);

        Try<User> userTry = underTest.removeUserAssociation(admin, constituency.getId(), user.getId());

        assertThat(userTry.isSuccess(), is(false));
        assertThat(userTry.getFailure(), is(instanceOf(NotFoundFailure.class)));
    }

    @Test
    public void returnsNotAuthorizedIfNonAdminWhenRemoveConstituencyFromUser() throws Exception {
        User admin = user().withRole(Role.USER).build();
        User user = user().withConstituencies(new HashSet<>()).build();
        Constituency constituency = constituency().build();
        given(constituencyRepository.findOne(constituency.getId())).willReturn(constituency);
        given(userRepository.findOne(user.getId())).willReturn(user);

        Try<User> userTry = underTest.removeUserAssociation(admin, constituency.getId(), user.getId());

        assertThat(userTry.isSuccess(), is(false));
        assertThat(userTry.getFailure(), is(instanceOf(NotAuthorizedFailure.class)));
    }

}