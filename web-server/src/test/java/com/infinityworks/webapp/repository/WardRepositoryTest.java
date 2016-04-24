package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.UUID;

import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:sql/drop-create.sql",
                        "classpath:sql/constituencies.sql",
                        "classpath:sql/wards.sql",
                        "classpath:sql/users.sql"
                })
})
public class WardRepositoryTest extends RepositoryTest {

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private ConstituencyRepository constituencyRepository;

    @Test
    public void returnsTheWards() throws Exception {
        List<Ward> all = wardRepository.findAll();

        assertThat(all, hasSize(greaterThan(0)));
    }

    @Test
    public void returnsWardByConstituencyCodeAndWardNames() throws Exception {
        Constituency constituency = constituencyRepository.findOne(UUID.fromString("0d338b99-3d15-44f7-904f-3ebc18a7ab4a"));
        List<Ward> wards = wardRepository.findByConstituencyAndNameLike(
                constituency, "Earlsd");

        assertThat(wards, hasSize(1));

        Ward earlsdon = ward()
                .withWardName("Earlsdon")
                .withWardCode("E05001221")
                .build();

        assertThat(wards, hasItem(earlsdon));
    }

    @Test
    public void returnsWardsByConstituency() throws Exception {
        Constituency constituency = constituencyRepository.findOne(
                UUID.fromString("0d338b99-3d15-44f7-904f-3ebc18a7ab4a"));
        List<Ward> wards = wardRepository.findByConstituencyOrderByNameAsc(constituency);

        assertThat(wards, hasSize(6));

        Ward earlsdon = ward()
                .withWardName("Earlsdon")
                .withWardCode("E05001221")
                .build();

        assertThat(wards, hasItem(earlsdon));
    }
}