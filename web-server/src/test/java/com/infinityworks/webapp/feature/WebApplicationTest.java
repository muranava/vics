package com.infinityworks.webapp.feature;


import com.infinityworks.webapp.Application;
import com.infinityworks.webapp.testsupport.stub.PafServer;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.repository.config.RepositoryConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {Application.class, RepositoryConfiguration.class})
public abstract class WebApplicationTest {
    protected MockMvc mockMvc;
    protected final PafServer pafApiStub = new PafServer();

    @Autowired
    protected WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(context).build();
        pafApiStub.start();
    }

    @After
    public void tearDown() {
        pafApiStub.stop();
    }
}
