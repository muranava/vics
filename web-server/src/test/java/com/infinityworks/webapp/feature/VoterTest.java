package com.infinityworks.webapp.feature;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.pdf.CanvassTableConfig;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.pdf.renderer.LogoRenderer;
import com.infinityworks.webapp.rest.VoterController;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.RecordVoteRequest;
import com.infinityworks.webapp.rest.dto.StreetRequest;
import com.infinityworks.webapp.service.*;
import com.infinityworks.webapp.testsupport.builder.downstream.RecordVoteBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;

import static com.infinityworks.webapp.common.JsonUtil.objectMapper;
import static com.infinityworks.webapp.testsupport.builder.downstream.ElectorsByStreetsRequestBuilder.electorsByStreets;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:sql/drop-create.sql",
                        "classpath:sql/regions.sql",
                        "classpath:sql/constituencies.sql",
                        "classpath:sql/wards.sql",
                        "classpath:sql/users.sql",
                        "classpath:sql/record-contact-logs.sql"
                })
})
public class VoterTest extends WebApplicationTest {
    private static final Logger log = LoggerFactory.getLogger(VoterTest.class);
    private SessionService sessionService;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        VoterService voterService = getBean(VoterService.class);
        RequestValidator requestValidator = getBean(RequestValidator.class);
        RecordVotedService recordVotedService = getBean(RecordVotedService.class);
        RecordContactService contactService = getBean(RecordContactService.class);
        TableBuilder tableBuilder = new TableBuilder(new CanvassTableConfig());
        DocumentBuilder documentBuilder = new DocumentBuilder(mock(LogoRenderer.class), new CanvassTableConfig());
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

        List<StreetRequest> townStreets = emptyList();
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

        List<StreetRequest> townStreets = null;
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
        pafApiStub.willSearchVoters("CV46PL", "McCall", "E05001221");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("surname", "McCall");
        params.add("postcode", "CV46PL");
        params.add("wardCode", "E05001221");
        UriComponents uriComponents = UriComponentsBuilder.fromPath("/elector")
                .queryParams(params)
                .build();

        String url = uriComponents.toUriString();

        mockMvc.perform(get(url)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].full_name", is("McCall, John B")))
                .andExpect(jsonPath("$[0].first_name", is("John B")))
                .andExpect(jsonPath("$[0].surname", is("McCall")))
                .andExpect(jsonPath("$[0].ern", is("E050097474-LFF-305-0")))
                .andExpect(jsonPath("$[0].address.postcode", is("CV4 6PL")))
                .andExpect(jsonPath("$[0].address.line_1", is("Grange Farm House")))
                .andExpect(jsonPath("$[0].address.line_2", is("Crompton Lane")))
                .andExpect(jsonPath("$[0].address.post_town", is("Coventry")));
    }

    @Test
    public void recordsVoted() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));
        pafApiStub.willRecordVoterVoted("E05001221-ADD-1313-1");

        RecordVoteRequest request = new RecordVoteBuilder()
                .withWardCode("E05001221")
                .withErn("ADD-1313-1")
                .build();
        String requestBody = objectMapper.writeValueAsString(request);
        log.info("Request: " + requestBody);

        mockMvc.perform(post("/elector/voted")
                .content(requestBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("wardCode", is("E05001221")))
                .andExpect(jsonPath("ern", is("ADD-1313-1")));
    }

    @Test
    public void undoVoted() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));
        pafApiStub.willUndoVoterVoted("E05001221-ADD-1313-1");


        mockMvc.perform(delete("/elector/E05001221-ADD-1313-1/voted")
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("wardCode", is("E05001221")))
                .andExpect(jsonPath("ern", is("ADD-1313-1")));
    }
}
