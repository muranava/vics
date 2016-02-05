package com.infinityworks.webapp.feature.electors;

import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.feature.WebApplicationTest;
import com.infinityworks.webapp.rest.dto.ElectorsByWardAndConstituencyRequest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {"classpath:drop-create.sql", "classpath:wards.sql", "classpath:electors-with-addresses.sql"})
})
public class ElectorsTest extends WebApplicationTest {
    private final Logger log = LoggerFactory.getLogger(ElectorsTest.class);

    @Test
    public void returnsThePreviewForTheGivenWardsAndConstituency() throws Exception {
        String endpoint = "/elector/local";
        ElectorsByWardAndConstituencyRequest request = new ElectorsByWardAndConstituencyRequest(asList("Upwood and The Raveleys", "Warboys and Bury"), "North West Cambridgeshire");
        String content = objectMapper.writeValueAsString(request);

        log.info(content);

        ResultActions result = mockMvc.perform(post(endpoint)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        MvcResult mvcResult = result.andExpect(status().isOk()).andReturn();
        List<Ward> wards = asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Ward[].class));

        Ward upwood = ward().withWardName("Upwood and The Raveleys")
                .withWardCode("E05008584")
                .withConstituencyName("North West Cambridgeshire")
                .withConstituencyCode("E14000855")
                .build();

        Ward warboys = ward().withWardName("Warboys and Bury")
                .withWardCode("E05008939")
                .withConstituencyName("North West Cambridgeshire")
                .withConstituencyCode("E14000855")
                .build();

        assertThat(wards, hasSize(2));
        assertThat(wards, hasItem(upwood));
        assertThat(wards, hasItem(warboys));
    }

    @Test
    public void returnsEmptyWardsListForTheGivenWardsAndConstituencyIfNotFound() throws Exception {
        String endpoint = "/elector/local";
        ElectorsByWardAndConstituencyRequest request = new ElectorsByWardAndConstituencyRequest(asList("I dont exist", "me neither"), "North West Cambridgeshire");
        String content = objectMapper.writeValueAsString(request);

        log.info(content);

        ResultActions result = mockMvc.perform(post(endpoint)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        MvcResult mvcResult = result.andExpect(status().isOk()).andReturn();
        List<Ward> wards = asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Ward[].class));

        assertThat(wards, hasSize(0));
    }
}
