package com.infinityworks.webapp.feature;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.pdf.CanvassTableConfig;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.pdf.renderer.FlagsKeyRenderer;
import com.infinityworks.webapp.pdf.renderer.LogoRenderer;
import com.infinityworks.webapp.rest.VoterController;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.RecordVote;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.service.ContactService;
import com.infinityworks.webapp.service.RecordVotedService;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.VoterService;
import com.infinityworks.webapp.testsupport.builder.downstream.RecordVoteBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static com.infinityworks.webapp.testsupport.builder.downstream.ElectorsByStreetsRequestBuilder.electorsByStreets;
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
public class VoterTest extends WebApplicationTest {
    private SessionService sessionService;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        VoterService voterService = getBean(VoterService.class);
        RequestValidator requestValidator = getBean(RequestValidator.class);
        RecordVotedService recordVotedService = getBean(RecordVotedService.class);
        ContactService contactService = getBean(ContactService.class);
        TableBuilder tableBuilder = new TableBuilder(new CanvassTableConfig());
        DocumentBuilder documentBuilder = new DocumentBuilder(mock(LogoRenderer.class), getBean(FlagsKeyRenderer.class), new CanvassTableConfig());
        VoterController wardController = new VoterController(tableBuilder, documentBuilder, voterService, requestValidator, recordVotedService, contactService, sessionService, new RestErrorHandler());

        mockMvc = MockMvcBuilders
                .standaloneSetup(wardController)
                .build();
        pafApiStub.start();
    }

    @Test
    public void returnsBadRequestIfStreetsIsEmpty() throws Exception {
        String wardCode = "E05001221";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(earlsdon()));
        pafApiStub.willReturnVotersByWardByTownAndByStreet(wardCode, "Coventry");

        List<Street> townStreets = emptyList();
        ElectorsByStreetsRequest request = electorsByStreets().withStreets(townStreets).build();
        String url = "/elector/ward/" + wardCode + "/street/pdf";

        mockMvc.perform(post(url)
                .accept("application/pdf")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfStreetsNull() throws Exception {
        String wardCode = "E05001221";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        List<Street> townStreets = null;
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
                .thenReturn(Try.success(earlsdon()));
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
                .andExpect(status().isOk()); // TODO implement assertions when API defined
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

    @Test
    public void recordsVoted() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));
        pafApiStub.willRecordVoterVoted("E05001221-ADD-1313-1");

        RecordVote request = new RecordVoteBuilder().withWardCode("E05001221").withWardName("Earlsdon").withErn("ADD-1313-1").withSuccess(true).build();
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/elector/voted")
                .content(requestBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("wardCode", is("E05001221")))
                .andExpect(jsonPath("wardName", is("Earlsdon")))
                .andExpect(jsonPath("ern", is("ADD-1313-1")))
                .andExpect(jsonPath("success", is(true)));
    }
}
