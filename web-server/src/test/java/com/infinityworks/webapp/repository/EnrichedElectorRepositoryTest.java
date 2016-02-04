package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.ElectorWithAddress;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {"classpath:drop-create.sql", "classpath:electors-with-addresses.sql"})
})
public class EnrichedElectorRepositoryTest extends RepositoryTest {

    @Autowired
    private EnrichedElectorRepository enrichedElectorRepository;

    @Test
    public void returnsTheElectorsInTheGivenWardsOrderedByWardCodeAscStreetAsc() throws Exception {
        List<String> wardCodes = asList("E09", "E05000027");

        List<ElectorWithAddress> electors =
                enrichedElectorRepository.findByWardCodeInOrderByWardCodeAscStreetAscHouseAsc(wardCodes);

        assertThat(electors, hasSize(greaterThan(0)));
        assertThat(electors.get(0).getWardCode(), is("E05000027"));
        assertThat(electors.get(0).getStreet(), is("Amber Road"));
        assertThat(electors.get(1).getStreet(), is("Armitage Road"));
        assertThat(electors.get(1).getHouse(), is("55"));
        assertThat(electors.get(2).getStreet(), is("Armitage Road"));
        assertThat(electors.get(2).getHouse(), is("59"));
        assertThat(electors.get(3).getStreet(), is("Ascott Drive"));
        assertThat(electors.get(4).getWardCode(), is("E09"));
    }
}
