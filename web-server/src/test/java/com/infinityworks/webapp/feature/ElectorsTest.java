package com.infinityworks.webapp.feature;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.ElectorsController;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.ElectorsService;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.VoteService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static com.infinityworks.webapp.testsupport.Fixtures.abbotRoad;
import static com.infinityworks.webapp.testsupport.Fixtures.kirbyRoad;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class ElectorsTest extends WebApplicationTest {
    private SessionService sessionService;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        ElectorsService electorsService = getBean(ElectorsService.class);
        RequestValidator requestValidator = getBean(RequestValidator.class);
        VoteService voteService = getBean(VoteService.class);
        ElectorsController wardController = new ElectorsController(electorsService, requestValidator, voteService, sessionService, new RestErrorHandler());

        mockMvc = MockMvcBuilders
                .standaloneSetup(wardController)
                .build();
        pafApiStub.start();
    }

    @Test
    @Ignore
    public void returnsTheElectorsByStreetAndTown() throws Exception {
        String wardCode = "E05001221";
        String town = "Coventry";
        pafApiStub.willReturnVotersByWardByTownAndByStreet(wardCode, town);
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        TownStreets townStreets = new TownStreets(asList(kirbyRoad(), abbotRoad()));
        String url = "/elector/ward/" + wardCode + "/street/pdf";

        ResultActions response = mockMvc.perform(post(url)
                .accept("application/pdf")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(townStreets)));

        response.andExpect(status().isOk());
    }

    @Test
    public void returnsBadRequestIfStreetsIsEmpty() throws Exception {
        String wardCode = "E05001221";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        TownStreets townStreets = new TownStreets(emptyList());
        String url = "/elector/ward/" + wardCode + "/street/pdf";

        mockMvc.perform(post(url)
                .accept("application/pdf")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(townStreets)))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void returnsBadRequestIfStreetsNull() throws Exception {
        String wardCode = "E05001221";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        TownStreets townStreets = null;
        String url = "/elector/ward/" + wardCode + "/street/pdf";

        mockMvc.perform(post(url)
                .accept("application/pdf")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(townStreets)))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void returnsTheElectorsWhenSearchingByAttributes() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));
        pafApiStub.willSearchVoters("McCall", "KT25BU");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("lastName", "McCall");
        params.add("postCode", "KT25BU");
        params.add("wardCode", "E05001221");
        UriComponents uriComponents = UriComponentsBuilder.fromPath("/elector")
                .queryParams(params)
                .build();

        String url = uriComponents.toUriString();

        mockMvc.perform(get(url)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pollingDistrict", is("AB")))
                .andExpect(jsonPath("$[0].electorId", is("22217bf4")))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("McCall")));
    }

    @Test
    public void searchReturnsNotAuthorizedIfElectorHasNoWardPermission() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));
        pafApiStub.willSearchVoters("McCall", "KT25BU");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("lastName", "McCall");
        params.add("postCode", "KT25BU");
        params.add("wardCode", "E05001235");
        UriComponents uriComponents = UriComponentsBuilder.fromPath("/elector")
                .queryParams(params)
                .build();

        String url = uriComponents.toUriString();

        mockMvc.perform(get(url)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void searchReturnsNotFoundIfWardInvalid() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));
        pafApiStub.willSearchVoters("McCall", "KT25BU");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("lastName", "McCall");
        params.add("postCode", "KT25BU");
        params.add("wardCode", "A05001235");
        UriComponents uriComponents = UriComponentsBuilder.fromPath("/elector")
                .queryParams(params)
                .build();

        String url = uriComponents.toUriString();

        mockMvc.perform(get(url)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
