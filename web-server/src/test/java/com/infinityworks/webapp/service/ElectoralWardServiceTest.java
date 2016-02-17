package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.repository.WardRepository;
import com.infinityworks.webapp.rest.dto.UserRestrictedWards;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Sets.newHashSet;
import static com.infinityworks.webapp.testsupport.builder.ConstituencyBuilder.constituency;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ElectoralWardServiceTest {
    private ConstituencyRepository constituencyRepository;
    private WardService underTest;
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        WardRepository wardRepository = mock(WardRepository.class);
        constituencyRepository = mock(ConstituencyRepository.class);
        userRepository = mock(UserRepository.class);
        underTest = new WardService(wardRepository, userRepository, constituencyRepository);
    }

    @Test
    public void returnsTheWards() throws Exception {
        User user = mock(User.class);
        Constituency c = constituency().build();
        given(constituencyRepository.findOne(c.getId())).willReturn(c);
        given(user.getWards()).willReturn(newHashSet(
                ward().withWardName("Willenhall").withConstituency(c).build(),
                ward().withWardName("Binley").withConstituency(c).build()));

        Try<UserRestrictedWards> result = underTest.findByConstituency(c.getId(), user);

        List<Ward> wards = new ArrayList<>(result.get().getWards());
        assertThat(wards.get(0).getName(), is("Willenhall"));
        assertThat(wards.get(1).getName(), is("Binley"));
    }

    @Test
    public void returnsFailureIfConstituencyNotFound() throws Exception {
        User user = mock(User.class);
        Constituency c = constituency().build();
        given(constituencyRepository.findOne(c.getId())).willReturn(null);

        Try<UserRestrictedWards> result = underTest.findByConstituency(c.getId(), user);

        assertThat(result.isSuccess(), is(false));
    }
}
