package com.infinityworks.webapp.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.math.BigInteger;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:drop-create.sql",
                        "classpath:constituencies.sql",
                        "classpath:wards.sql",
                        "classpath:users.sql",
                        "classpath:record-contact-logs.sql"
                })
})
public class StatsRepositoryTest extends RepositoryTest {

    @Autowired
    private StatsRepository repository;

    @Test
    public void returnsTheTopCanvassers() throws Exception {
        int limit = 5;
        List<Object[]> logsByUser = repository.countRecordContactByUser(limit);

        assertThat(logsByUser, hasSize(lessThanOrEqualTo(limit)));
        assertThat(logsByUser.get(0)[1], is("Dion"));
        assertThat(logsByUser.get(0)[2], is("Dublin"));
        assertThat(logsByUser.get(0)[3], is(BigInteger.valueOf(6)));

        assertThat(logsByUser.get(1)[3], is(BigInteger.valueOf(2)));
        assertThat(logsByUser.get(2)[3], is(BigInteger.valueOf(1)));
        assertThat(logsByUser.get(3)[3], is(BigInteger.valueOf(1)));
        assertThat(logsByUser.get(4)[3], is(BigInteger.valueOf(1)));
    }

    @Test
    public void returnsTheTopConstituencies() throws Exception {
        int limit = 5;
        List<Object[]> logsByUser = repository.countMostRecordContactByConstituency(limit);

        assertThat(logsByUser, hasSize(lessThanOrEqualTo(limit)));
        assertThat(logsByUser.get(0)[0], is("Coventry South"));
        assertThat(logsByUser.get(0)[1], is(BigInteger.valueOf(10)));

        assertThat(logsByUser.get(1)[0], is("Richmond Park"));
        assertThat(logsByUser.get(1)[1], is(BigInteger.valueOf(2)));
    }

    @Test
    public void returnsTheTopWards() throws Exception {
        int limit = 5;
        List<Object[]> logsByUser = repository.countMostRecordContactByWard(limit);

        assertThat(logsByUser, hasSize(lessThanOrEqualTo(limit)));
        assertThat(logsByUser.get(0)[0], is("Wainbody"));
        assertThat(logsByUser.get(0)[1], is(BigInteger.valueOf(9)));

        assertThat(logsByUser.get(1)[0], is("Canbury"));
        assertThat(logsByUser.get(1)[1], is(BigInteger.valueOf(2)));

        assertThat(logsByUser.get(2)[0], is("Binley and Willenhall"));
        assertThat(logsByUser.get(2)[1], is(BigInteger.valueOf(1)));
    }
}
