package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.rest.dto.ElectorPreviewRequest;
import com.infinityworks.webapp.rest.dto.VoterPreview;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {"classpath:drop-create.sql", "classpath:wards.sql", "classpath:electors.sql"})
})
public class ElectorsTest extends WebApplicationTest {
    private final Logger log = LoggerFactory.getLogger(ElectorsTest.class);

    @Test
    public void returnsBadRequestIfTheBodyIsEmpty() throws Exception {
        String endpoint = "/elector/preview";

        ResultActions result = mockMvc.perform(post(endpoint)
                .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfConstituencyNameIsEmpty() throws Exception {
        String endpoint = "/elector/preview";
        ElectorPreviewRequest request = new ElectorPreviewRequest(singletonList("ABC"), "");
        String requestBody = objectMapper.writeValueAsString(request);

        log.info(requestBody);

        ResultActions result = mockMvc.perform(post(endpoint)
                .content(requestBody)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfConstituencyNameIsNotDefined() throws Exception {
        String endpoint = "/elector/preview";
        ElectorPreviewRequest request = new ElectorPreviewRequest(singletonList("ABC"), null);
        String content = objectMapper.writeValueAsString(request);

        log.info(content);

        ResultActions result = mockMvc.perform(post(endpoint)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsBadRequestIfWardsListContainsNullElement() throws Exception {
        String endpoint = "/elector/preview";
        ElectorPreviewRequest request = new ElectorPreviewRequest(asList("ABC", null), "constName");
        String content = objectMapper.writeValueAsString(request);

        log.info(content);

        ResultActions result = mockMvc.perform(post(endpoint)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void returnsThePreviewForTheGivenWardsAndConstituency() throws Exception {
        String endpoint = "/elector/preview";
        ElectorPreviewRequest request = new ElectorPreviewRequest(asList("Upwood and The Raveleys", "Warboys and Bury"), "North West Cambridgeshire");
        String content = objectMapper.writeValueAsString(request);

        log.info(content);

        ResultActions result = mockMvc.perform(post(endpoint)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        MvcResult mvcResult = result.andExpect(status().isOk()).andReturn();
        VoterPreview preview = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), VoterPreview.class);

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

        assertThat(preview.getWards(), hasSize(2));
        assertThat(preview.getWards(), hasItem(upwood));
        assertThat(preview.getWards(), hasItem(warboys));
    }

    @Test
    public void returnsEmptyWardsListForTheGivenWardsAndConstituencyIfNotFound() throws Exception {
        String endpoint = "/elector/preview";
        ElectorPreviewRequest request = new ElectorPreviewRequest(asList("I dont exist", "me neither"), "North West Cambridgeshire");
        String content = objectMapper.writeValueAsString(request);

        log.info(content);

        ResultActions result = mockMvc.perform(post(endpoint)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(print());

        MvcResult mvcResult = result.andExpect(status().isOk()).andReturn();
        VoterPreview preview = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), VoterPreview.class);

        assertThat(preview.getWards(), hasSize(0));
    }
}
