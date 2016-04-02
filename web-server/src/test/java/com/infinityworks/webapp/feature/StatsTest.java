package com.infinityworks.webapp.feature;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.rest.StatsController;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.StatsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class StatsTest extends WebApplicationTest {
    private SessionService sessionService;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        StatsService statsService = getBean(StatsService.class);
        StatsController ctrl = new StatsController(statsService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(ctrl)
                .build();
        pafApiStub.start();
    }

    @Test
    public void returns() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        mockMvc.perform(get("/stats/topcanvassers")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

}
