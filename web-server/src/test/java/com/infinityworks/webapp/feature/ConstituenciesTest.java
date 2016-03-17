package com.infinityworks.webapp.feature;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.rest.ConstituencyController;
import com.infinityworks.webapp.service.ConstituencyAssociationService;
import com.infinityworks.webapp.service.ConstituencyService;
import com.infinityworks.webapp.service.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private ConstituencyRepository constituencyRepository;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        ConstituencyService constituencyService =  getBean(ConstituencyService.class);
        ConstituencyAssociationService constituencyAssociationService =  getBean(ConstituencyAssociationService.class);
        ConstituencyController constituencyController = new ConstituencyController(constituencyService, constituencyAssociationService, new RestErrorHandler(), sessionService);

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
    public void returnsUnauthorizedIfUserNotAdminWehnSearching() throws Exception {
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

    @Test
    public void addsAUserAssociation() throws Exception {
        Constituency c = constituencyRepository.findOne(UUID.fromString("5e2636a6-991c-4455-b08f-93309533a2ab"));
        User covs = covs();
        User admin = admin();
        String endpoint = String.format("/constituency/%s/user/%s", c.getId(), covs.getId());

        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin));

        mockMvc.perform(post(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..constituencies[?(@.name =~ /.*Rugby/i)].id",
                           is(singletonList(c.getId().toString()))));
    }

    @Test
    public void removesAUserAssociation() throws Exception {
        Constituency c = constituencyRepository.findOne(UUID.fromString("0d338b99-3d15-44f7-904f-3ebc18a7ab4a"));
        User covs = covs();
        User admin = admin();
        String endpoint = String.format("/constituency/%s/user/%s", c.getId(), covs.getId());

        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin));

        mockMvc.perform(delete(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.constituencies", is(emptyList())));
    }
}
