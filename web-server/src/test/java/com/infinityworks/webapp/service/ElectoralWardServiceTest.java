package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.repository.WardRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.ConstituencyBuilder.constituency;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ElectoralWardServiceTest {
    private WardRepository wardRepository;
    private ConstituencyRepository constituencyRepository;
    private WardService underTest;

    @Before
    public void setUp() throws Exception {
        wardRepository = mock(WardRepository.class);
        constituencyRepository = mock(ConstituencyRepository.class);
        underTest = new WardService(wardRepository, constituencyRepository);
    }

    @Test
    public void returnsTheWards() throws Exception {
        Constituency c = constituency().build();
        given(constituencyRepository.findOne(c.getId())).willReturn(c);
        given(wardRepository.findByConstituencyOrderByNameAsc(c))
                .willReturn(asList(
                        ward().withWardName("Willenhall").build(),
                        ward().withWardName("Binley").build()));

        Try<List<Ward>> result = underTest.findByConstituency(c.getId());

        assertThat(result.get().get(0).getName(), is("Willenhall"));
        assertThat(result.get().get(1).getName(), is("Binley"));
    }

    @Test
    public void returnsFailureIfConstituencyNotFound() throws Exception {
        Constituency c = constituency().build();
        given(constituencyRepository.findOne(c.getId())).willReturn(null);

        Try<List<Ward>> result = underTest.findByConstituency(c.getId());

        assertThat(result.isSuccess(), is(false));
    }
}
