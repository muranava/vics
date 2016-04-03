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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    public void returnsTheTopCanvassers() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        mockMvc.perform(get("/stats/topcanvassers")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key", is("Dion Dublin")))
                .andExpect(jsonPath("$[0].count", is(6)))
                .andExpect(jsonPath("$[1].key", is("Ava McCall")))
                .andExpect(jsonPath("$[1].count", is(2)))
                .andExpect(jsonPath("$[2].key", is("Samir Ginola")))
                .andExpect(jsonPath("$[2].count", is(1)))
                .andExpect(jsonPath("$[3].key", is("Martin Freeman")))
                .andExpect(jsonPath("$[3].count", is(1)))
                .andExpect(jsonPath("$[4].key", is("Peter Ndlovu")))
                .andExpect(jsonPath("$[4].count", is(1)));
    }

    @Test
    public void returnsTheTopWards() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        mockMvc.perform(get("/stats/topwards")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key", is("Wainbody")))
                .andExpect(jsonPath("$[0].count", is(9)))
                .andExpect(jsonPath("$[1].key", is("Canbury")))
                .andExpect(jsonPath("$[1].count", is(2)))
                .andExpect(jsonPath("$[2].key", is("Binley and Willenhall")))
                .andExpect(jsonPath("$[2].count", is(1)));
    }

    @Test
    public void returnsTheTopConstituencies() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        mockMvc.perform(get("/stats/topconstituencies")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key", is("Coventry South")))
                .andExpect(jsonPath("$[0].count", is(10)))
                .andExpect(jsonPath("$[1].key", is("Richmond Park")))
                .andExpect(jsonPath("$[1].count", is(2)));
    }
}
