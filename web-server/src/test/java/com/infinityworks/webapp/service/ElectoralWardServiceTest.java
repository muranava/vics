package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.repository.WardRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ElectoralWardServiceTest {
    private WardRepository wardRepository;
    private ElectoralWardService underTest;

    @Before
    public void setUp() throws Exception {
        wardRepository = mock(WardRepository.class);
        underTest = new ElectoralWardService(wardRepository);
    }

    @Test
    public void returnsFailureIfRepositoryThrows() throws Exception {
        given(wardRepository.findByConstituencyNameIgnoreCase("Coventry South".toUpperCase()))
                .willThrow(new ConstraintViolationException("nope", null, null));

        Try<List<Ward>> result = underTest.findByConstituencyName("Coventry South");

        assertThat(result.getFailure(), instanceOf(ConstraintViolationException.class));
    }

    @Test
    public void returnsTheWards() throws Exception {
        given(wardRepository.findByConstituencyNameIgnoreCase("Coventry South".toUpperCase()))
                .willReturn(asList(
                        ward().withWardName("Willenhall").build(),
                        ward().withWardName("Binley").build()));

        Try<List<Ward>> result = underTest.findByConstituencyName("Coventry South");

        assertThat(result.isSuccess(), is(true));
        assertThat(result.get().get(0).getWardName(), is("Willenhall"));
        assertThat(result.get().get(1).getWardName(), is("Binley"));
    }
}