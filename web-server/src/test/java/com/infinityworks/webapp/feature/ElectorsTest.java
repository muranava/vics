package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.ElectorsController;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.ElectorsService;
import com.infinityworks.webapp.service.UserService;
import com.infinityworks.webapp.service.client.Property;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static com.infinityworks.webapp.testsupport.Fixtures.abbotRoad;
import static com.infinityworks.webapp.testsupport.Fixtures.kirbyRoad;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
                        "classpath:users.sql"})
})
public class ElectorsTest extends WebApplicationTest {
    protected UserService userService;

    @Before
    public void setup() {
        userService = mock(UserService.class);
        when(userService.getByEmail(anyString())).thenCallRealMethod();
        ElectorsService electorsService = getBean(ElectorsService.class);
        RequestValidator requestValidator = getBean(RequestValidator.class);
        ElectorsController wardController = new ElectorsController(electorsService, new RestErrorHandler(), requestValidator, userService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(wardController)
                .build();
        pafApiStub.start();
    }

    @Test
    public void returnsTheElectorsByStreetAndTown() throws Exception {
        String wardCode = "E05001221";
        String town = "Coventry";
        pafApiStub.willReturnVotersByWardByTownAndByStreet(wardCode, town);
        when(userService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        TownStreets townStreets = new TownStreets(asList(kirbyRoad(), abbotRoad()));
        String url = "/elector/ward/" + wardCode + "/street";

        ResultActions response = mockMvc.perform(post(url)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(townStreets)))
                .andDo(print());

        MvcResult result = response.andExpect(status().isOk()).andReturn();
        List<Property> wards = asList(objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Property[].class));

        assertThat(wards.get(0).getMainStreet(), is("Abbot Road"));
    }

    @Test
    public void returnsBadRequestIfStreetsIsEmpty() throws Exception {
        String wardCode = "E05001221";
        when(userService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        TownStreets townStreets = new TownStreets(emptyList());
        String url = "/elector/ward/" + wardCode + "/street";

        mockMvc.perform(post(url)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(townStreets)))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void returnsBadRequestIfStreetsNull() throws Exception {
        String wardCode = "E05001221";
        when(userService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        TownStreets townStreets = null;
        String url = "/elector/ward/" + wardCode + "/street";

        mockMvc.perform(post(url)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(townStreets)))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
    }
}
