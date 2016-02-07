package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.Application;
import com.infinityworks.webapp.config.Config;
import com.infinityworks.webapp.security.SecurityConfig;
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
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {
        Application.class,
        RepositoryConfiguration.class,
        SecurityConfig.class,
        Config.class
})
public abstract class WebApplicationTest {
    protected MockMvc mockMvc;
    protected final PafServer pafApiStub = new PafServer();

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
        pafApiStub.start();
    }

    protected RequestPostProcessor authenticatedUser() {
        return user("authenticatedUser");
    }

    @After
    public void tearDown() {
        pafApiStub.stop();
    }
}
