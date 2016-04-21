package com.infinityworks.webapp.feature;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.rest.dto.Flags;
import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.GotvController;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.service.GotvService;
import com.infinityworks.webapp.service.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static com.infinityworks.webapp.testsupport.builder.downstream.FlagsBuilder.flags;
import static com.infinityworks.webapp.common.JsonUtil.objectMapper;
import static com.infinityworks.webapp.testsupport.builder.downstream.ElectorsByStreetsRequestBuilder.electorsByStreets;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:drop-create.sql",
                        "classpath:constituencies.sql",
                        "classpath:wards.sql",
                        "classpath:users.sql",
                        "classpath:record-contact-logs.sql"})
})
public class GotvTest extends WebApplicationTest {
    private SessionService sessionService;
    private Logger log = LoggerFactory.getLogger(GotvTest.class);

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        GotvController wardController = new GotvController(getBean(RequestValidator.class), sessionService,
                getBean(GotvService.class), getBean(RestErrorHandler.class));

        mockMvc = MockMvcBuilders
                .standaloneSetup(wardController)
                .build();
        pafApiStub.start();
    }

    @Test
    public void returnsBadRequestIfIntentionRangeInvalid() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        Flags flags = flags().withIntentionFrom(0).withIntentionTo(3).build();
        ElectorsByStreetsRequest request = electorsByStreets()
                .withFlags(flags)
                .build();

        mockMvc.perform(post("/gotv/ward/E05001221/street/pdf")
                .accept("application/pdf")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfLikelihoodRangeInvalid() throws Exception {
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        Flags flags = flags().withLikelihoodFrom(2).withLikelihoodTo(6).build();
        ElectorsByStreetsRequest request = electorsByStreets()
                .withFlags(flags)
                .build();

        String content = objectMapper.writeValueAsString(request);
        log.info("Gotv canvass card request: {}", content);

        mockMvc.perform(post("/gotv/ward/E05001221/street/pdf")
                .accept("application/pdf")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
