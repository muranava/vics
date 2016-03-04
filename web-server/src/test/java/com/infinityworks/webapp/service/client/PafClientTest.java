package com.infinityworks.webapp.service.client;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.converter.PafToStreetConverter;
import com.infinityworks.webapp.paf.client.Http;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.client.PafErrorHandler;
import com.infinityworks.webapp.paf.dto.Property;
import com.infinityworks.webapp.paf.converter.StreetToPafConverter;
import com.infinityworks.webapp.paf.dto.PropertyResponse;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.testsupport.stub.PafServerStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
    private final CanvassConfig canvassConfig = mock(CanvassConfig.class);
    private PafClient pafClient;

    @Before
    public void setUp() throws Exception {
        when(canvassConfig.getPafApiBaseUrl()).thenReturn("http://localhost:9002/v1");
        RestTemplate restTemplate = new RestTemplate();
        Http http = new Http(restTemplate, canvassConfig, new PafErrorHandler());
        pafClient = new PafClient(new PafToStreetConverter(), http, canvassConfig, new StreetToPafConverter());
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
        List<Street> townStreets = asList(kirbyRoad(), abbotRoad());

        Try<PropertyResponse> electorsByStreet = pafClient.findVotersByStreet(townStreets, "E05001221");

        assertThat(electorsByStreet.isSuccess(), is(true));
        List<List<Property>> properties = electorsByStreet.get().response();
        assertThat(properties.get(0).get(0).voters().get(0).lastName(), is("Deaux"));
    }
}
