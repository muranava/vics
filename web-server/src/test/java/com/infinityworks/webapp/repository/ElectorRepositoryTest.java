package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Elector;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {"classpath:drop-create.sql", "classpath:electors.sql"})
})
public class ElectorRepositoryTest extends RepositoryTest {

    @Autowired
    private ElectorRepository repository;

    @Test
    public void shouldRetrieveTheElectors() throws Exception {
        List<Elector> all = repository.findAll();

        assertThat(all, hasSize(greaterThan(0)));
    }

    @Test
    public void returnsTheElectorsInTheGivenWard() throws Exception {
        List<String> wardCodes = singletonList("E14000639");

        List<Elector> electors = repository.findByWardCodeIn(wardCodes);

        assertThat(electors, hasSize(16));
        electors.forEach(elector -> assertThat(elector.getWardCode(), is("E14000639")));
    }

    @Test
    public void returnsTheElectorsInTheGivenWards() throws Exception {
        List<String> wardCodes = asList("E14000639", "E05000027");

        List<Elector> electors = repository.findByWardCodeIn(wardCodes);

        assertThat(electors, hasSize(19));
        electors.forEach(elector -> assertThat(elector.getWardCode(), isOneOf("E14000639", "E05000027")));
    }

    @Test
    public void returnsEmptyListIfGivenWardsNotFound() throws Exception {
        List<String> wardCodes = singletonList("IDONTEXIST");

        List<Elector> electors = repository.findByWardCodeIn(wardCodes);

        assertThat(electors, hasSize(0));
    }
}
