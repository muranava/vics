package com.infinityworks.webapp.feature.electors;

import com.infinityworks.webapp.feature.WebApplicationTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {"classpath:drop-create.sql", "classpath:wards.sql", "classpath:electors-with-addresses.sql"})
})
public class ElectorsTest extends WebApplicationTest {

}
