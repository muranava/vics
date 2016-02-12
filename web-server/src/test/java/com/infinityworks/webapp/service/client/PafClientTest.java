package com.infinityworks.webapp.service.client;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.testsupport.Fixtures;
import com.infinityworks.webapp.testsupport.stub.PafServerStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.infinityworks.webapp.testsupport.Fixtures.abbotRoad;
import static com.infinityworks.webapp.testsupport.Fixtures.kirbyRoad;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PafClientTest {

    private final PafServerStub pafApiStub = new PafServerStub();
    private CanvassConfig canvassConfig = mock(CanvassConfig.class);
    private PafClient pafClient;

    @Before
    public void setUp() throws Exception {
        when(canvassConfig.getPafApiBaseUrl()).thenReturn("http://localhost:9002/v1");
        pafClient = new PafClient(new RestTemplate(), canvassConfig, new HttpHeaders());
        pafApiStub.start();
    }

    @After
    public void tearDown() throws Exception {
        pafApiStub.stop();
    }

    @Test
    public void returnsTheStreetsByWardCode() throws Exception {
        pafApiStub.willReturnStreetsByWard("E05001221");

        Try<List<Street>> streets = pafClient.findStreetsByWardCode("E05001221");

        assertThat(streets.get().isEmpty(), is(false));
        streets.get().stream().forEach(street -> assertThat(street.getPostTown(), is("Coventry")));
    }

    @Test
    public void returnsTheVotersByWardAndStreet() throws Exception {
        pafApiStub.willReturnVotersByWardByTownAndByStreet("E05001221", "Coventry");
        TownStreets townStreets = new TownStreets(asList(kirbyRoad(), abbotRoad()));

        Try<List<Property>> electorsByStreet = pafClient.findElectorsByStreet(townStreets, "E05001221");

        assertThat(electorsByStreet.isSuccess(), is(true));
        List<Property> properties = electorsByStreet.get();
        assertThat(properties.get(0).getVoters().get(0).getLastName(), is("Deaux"));
    }
}
