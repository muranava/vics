package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Ward;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {"classpath:drop-create.sql", "classpath:wards.sql"})
})
public class WardRepositoryTest extends RepositoryTest {

    @Autowired
    private WardRepository wardRepository;

    @Test
    public void returnsTheWards() throws Exception {
        List<Ward> all = wardRepository.findAll();

        assertThat(all, hasSize(greaterThan(0)));
    }

    @Test
    public void findsTheWardsByConstituencyName() throws Exception {
        List<Ward> all = wardRepository.findByConstituencyName("Dagenham and Rainham".toUpperCase());

        assertThat(all, hasSize(2));

        Ward eastbrook = ward().withWardName("Eastbrook")
                .withWardCode("E05000030")
                .withConstituencyName("Dagenham and Rainham")
                .withConstituencyCode("E14000657")
                .build();

        Ward chadwell = ward().withWardName("Eastbrook")
                .withWardCode("E05000030")
                .withConstituencyName("Dagenham and Rainham")
                .withConstituencyCode("E14000657")
                .build();

        assertThat(all, hasItem(eastbrook));
        assertThat(all, hasItem(chadwell));
    }

    @Test
    public void findByConstituencyNameReturnsEmptyListIfNotFound() throws Exception {
        List<Ward> all = wardRepository.findByConstituencyName("I dont exist");

        assertThat(all, hasSize(0));
    }
}