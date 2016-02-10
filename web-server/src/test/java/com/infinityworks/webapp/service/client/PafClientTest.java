package com.infinityworks.webapp.service.client;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.config.CanvassConfig;
import com.infinityworks.webapp.testsupport.stub.PafServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PafClientTest {

    private final PafServer pafApiStub = new PafServer();
    private CanvassConfig canvassConfig = mock(CanvassConfig.class);
    private PafClient pafClient;

    @Before
    public void setUp() throws Exception {
        when(canvassConfig.getPafApiBaseUrl()).thenReturn("http://localhost:9002/v1");
        pafClient = new PafClient(new RestTemplate(), canvassConfig);
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
}
