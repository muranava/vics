package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.repository.WardRepository;
import com.infinityworks.webapp.rest.ConstituencyController;
import com.infinityworks.webapp.rest.WardController;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.rest.dto.UserRestrictedWards;
import com.infinityworks.webapp.service.AddressService;
import com.infinityworks.webapp.service.ConstituencyService;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.WardService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
                        "classpath:users.sql"})
})
public class ConstituenciesTest extends WebApplicationTest {
    private SessionService sessionService;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        ConstituencyService constituencyService = getBean(ConstituencyService.class);
        ConstituencyController constituencyController = new ConstituencyController(constituencyService, new RestErrorHandler(), sessionService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(constituencyController)
                .build();
        pafApiStub.start();
    }

    @Test
    public void returnsTheConstituenciesByName() throws Exception {
        String name = "north";
        String limit = "2";
        String endpoint = String.format("/constituency/search?limit=%s&name=%s", limit, name);

        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Coventry North East")))
                .andExpect(jsonPath("$[1].name", is("Coventry North West")));
    }

    @Test
    public void returnsUnauthorizedIfUserNotAdmin() throws Exception {
        String name = "north";
        String limit = "2";
        String endpoint = String.format("/constituency/search?limit=%s&name=%s", limit, name);

        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
