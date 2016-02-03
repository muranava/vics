package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.domain.Ward;
import org.junit.Test;
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
                scripts = {"classpath:drop-create.sql", "classpath:wards.sql"})
})
public class WardsTest extends WebApplicationTest {

    @Test
    public void returnsTheWards() throws Exception {
        String name = "South Cambridgeshire";
        String endpoint = format("/ward?constituency=%s", name);

        ResultActions response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = response.andExpect(status().isOk()).andReturn();
        List<Ward> wards = asList(objectMapper.readValue(result.getResponse().getContentAsString(), Ward[].class));

        assertThat(wards.get(0).getWardName(), is("Bar Hill"));
        assertThat(wards.get(4).getWardName(), is("Comberton"));
        assertThat(wards.size(), is(10));

        wards.forEach(ward -> assertThat(ward.getConstituencyName(), is(name)));
    }

    @Test
    public void returnsEmptyListIfNotFound() throws Exception {
        String name = "Non-existant ward";
        String endpoint = format("/ward?constituency=%s", name);

        ResultActions response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = response.andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getStatus(), is(200));
        List<Ward> wards = asList(objectMapper.readValue(result.getResponse().getContentAsString(), Ward[].class));
        assertThat(wards, is(emptyList()));
    }

    @Test
    public void returnsBadRequestIfNameIsAbsent() throws Exception {
        String endpoint = "/ward";

        ResultActions response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        response.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfNameIsLessThanMin() throws Exception {
        String endpoint = "/ward?constituency=A";

        ResultActions response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        response.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfNameIsGreaterThanMax() throws Exception {
        String endpoint = "/ward?constituency=" + new String(new char[70]).replace("\0", "*");

        ResultActions response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        response.andExpect(status().isBadRequest());
    }
}
