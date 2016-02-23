package com.infinityworks.webapp.feature;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.WardController;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.rest.dto.UserRestrictedWards;
import com.infinityworks.webapp.rest.dto.WardSummary;
import com.infinityworks.webapp.service.AddressService;
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
public class WardsTest extends WebApplicationTest {
    private SessionService sessionService;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        WardService wardService = getBean(WardService.class);
        AddressService addressService = getBean(AddressService.class);
        WardController wardController = new WardController(sessionService, wardService, new RestErrorHandler(), addressService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(wardController)
                .build();
        pafApiStub.start();
    }

    @Test
    public void returnsTheRestrictedWardsForCovs() throws Exception {
        String endpoint = "/ward";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        ResultActions response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = response.andExpect(status().isOk()).andReturn();
        UserRestrictedWards wards = objectMapper.readValue(result.getResponse().getContentAsString(), UserRestrictedWards.class);

        assertThat(wards.getWards().size(), is(6));
    }

    @Test
    public void returnsTheWardsSummariesForCovs() throws Exception {
        String endpoint = "/ward?summary=true";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        ResultActions response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = response.andExpect(status().isOk()).andReturn();
        WardSummary[] wards = objectMapper.readValue(result.getResponse().getContentAsString(), WardSummary[].class);

        assertThat(wards[0].getName(), is("Binley and Willenhall"));
        assertThat(wards[0].getCode(), is("E05001219"));
        assertThat(wards[0].getConstituencyName(), is("Coventry South"));
    }

    @Test
    public void returnsStreetsByWard() throws Exception {
        String wardCode = "E05001221";
        String endpoint = "/ward/" + wardCode + "/street";
        pafApiStub.willReturnStreetsByWard(wardCode);
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        ResultActions resultActions = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andExpect(status().isOk()).andReturn();

        List<Street> streets = asList(objectMapper.readValue(result.getResponse().getContentAsString(), Street[].class));
        assertThat(streets.size(), is(6));
    }

    @Test
    public void returnsTheWardsByName() throws Exception {
        String name = "wood";
        String limit = "2";
        String endpoint = String.format("/ward/search?limit=%s&name=%s", limit, name);

        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Westwood")))
                .andExpect(jsonPath("$[1].name", is("Woodlands")));
    }

    @Test
    public void returnsFailureIfNotAdminWhenSearchingByName() throws Exception {
        String name = "wood";
        String limit = "2";
        String endpoint = String.format("/ward/search?limit=%s&name=%s", limit, name);

        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void returns404IfConstituencyNotFound() throws Exception {
        String idontExist = "922268a5-5689-418d-b63d-b21545345f01";
        String endpoint = "/constituency/" + idontExist + "/ward";

        mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void returns404IfInvalidUUID() throws Exception {
        String invalidUUID = "968a5-5689-418d-b63d-b21545345f01";
        String endpoint = "/constituency/" + invalidUUID + "/ward";

        mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
