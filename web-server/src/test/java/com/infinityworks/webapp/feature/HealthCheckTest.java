package com.infinityworks.webapp.feature;


import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:drop-create.sql"})
public class HealthCheckTest extends WebApplicationTest {
    @Test
    public void returnsTheHealthStatus() throws Exception {
        String healthEndpoint = "/health";

        ResultActions response = mockMvc.perform(get(healthEndpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("status", is("UP")));
    }
}