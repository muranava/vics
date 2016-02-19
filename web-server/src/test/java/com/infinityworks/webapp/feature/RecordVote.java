package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.VotedController;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.VoteService;
import org.junit.Before;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;

public class RecordVote extends WebApplicationTest {
    private SessionService sessionService;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        VotedController wardController =
                new VotedController(sessionService, getBean(VoteService.class), getBean(RestErrorHandler.class));
        mockMvc = MockMvcBuilders
                .standaloneSetup(wardController)
                .build();
        pafApiStub.start();
    }
}
