package com.infinityworks.webapp.feature.electors;

import com.infinityworks.webapp.feature.WebApplicationTest;
import com.infinityworks.webapp.rest.dto.ElectorsByWardAndConstituencyRequest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.ResultActions;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {"classpath:drop-create.sql", "classpath:wards.sql", "classpath:electors.sql"})
})
public class ElectorsFaliureScenariosTest extends WebApplicationTest {
    private final Logger log = LoggerFactory.getLogger(ElectorsFaliureScenariosTest.class);

    @Test
    public void returnsBadRequestIfTheBodyIsEmpty() throws Exception {
        String endpoint = "/elector";

        ResultActions result = mockMvc.perform(post(endpoint)
                .with(authenticatedUser())
                .with(csrf())
                .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfConstituencyNameIsEmpty() throws Exception {
        String endpoint = "/elector";
        ElectorsByWardAndConstituencyRequest request = new ElectorsByWardAndConstituencyRequest(singletonList("ABC"), "");
        String requestBody = objectMapper.writeValueAsString(request);

        log.info(requestBody);

        ResultActions result = mockMvc.perform(post(endpoint)
                .with(authenticatedUser())
                .with(csrf())
                .content(requestBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfConstituencyNameIsNotDefined() throws Exception {
        String endpoint = "/elector";
        ElectorsByWardAndConstituencyRequest request = new ElectorsByWardAndConstituencyRequest(singletonList("ABC"), null);
        String content = objectMapper.writeValueAsString(request);

        log.info(content);

        ResultActions result = mockMvc.perform(post(endpoint)
                .with(authenticatedUser())
                .with(csrf())
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfWardsListContainsNullElement() throws Exception {
        String endpoint = "/elector";
        ElectorsByWardAndConstituencyRequest request = new ElectorsByWardAndConstituencyRequest(asList("ABC", null), "constName");
        String content = objectMapper.writeValueAsString(request);

        log.info(content);

        ResultActions result = mockMvc.perform(post(endpoint)
                .with(authenticatedUser())
                .with(csrf())
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }
}
