package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.domain.Ward;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {"classpath:drop-create.sql",  "classpath:constituencies.sql", "classpath:wards.sql"})
})
public class WardsTest extends WebApplicationTest {
    private final Logger log = LoggerFactory.getLogger(WardsTest.class);

    // TODO add test for ward name search

    @Test
    public void returnsTheWards() throws Exception {
        String endpoint = "/ward/constituency/911a68a5-5689-418d-b63d-b21545345f03";

        ResultActions response = mockMvc.perform(get(endpoint)
                .with(authenticatedUser())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = response.andExpect(status().isOk()).andReturn();
        List<Ward> wards = asList(objectMapper.readValue(result.getResponse().getContentAsString(), Ward[].class));

        assertThat(wards.get(0).getName(), is("Bablake"));

        wards.forEach(ward -> assertThat(ward.getConstituency().getName(), is("Coventry North West")));
    }

    @Test
    public void returns404IfConstituencyNotFound() throws Exception {
        String idontExist = "922268a5-5689-418d-b63d-b21545345f01";
        String endpoint = "/ward/constituency/" + idontExist;

        ResultActions response = mockMvc.perform(get(endpoint)
                .with(authenticatedUser())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = response.andExpect(status().isNotFound()).andReturn();

        assertThat(result.getResponse().getStatus(), is(404));
    }

    @Test
    public void returns404IfInvalidUUID() throws Exception {
        String invalidUUID = "968a5-5689-418d-b63d-b21545345f01";
        String endpoint = "/ward/constituency/" + invalidUUID;

        ResultActions response = mockMvc.perform(get(endpoint)
                .with(authenticatedUser())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        response.andExpect(status().isNotFound());
    }
}
