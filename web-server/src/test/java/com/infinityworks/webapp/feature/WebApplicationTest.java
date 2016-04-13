package com.infinityworks.webapp.feature;

import com.infinityworks.canvass.pafstub.PafServerStub;
import com.infinityworks.webapp.Application;
import com.infinityworks.webapp.config.Config;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.security.SecurityConfig;
import com.infinityworks.webapp.service.UserService;
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

import javax.servlet.Filter;

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
    protected final PafServerStub pafApiStub = new PafServerStub(9002);

    @Autowired
    protected WebApplicationContext applicationContext;

    @Autowired
    protected Filter springSecurityFilterChain;

    protected User admin() {
        UserService userService = getBean(UserService.class);
        return userService.getByUsername("me@admin.uk").get();
    }

    protected User covs() {
        UserService userService = getBean(UserService.class);
        return userService.getByUsername("cov@south.cov").get();
    }

    protected User earlsdon() {
        UserService userService = getBean(UserService.class);
        return userService.getByUsername("earlsdon@cov.uk").get();
    }

    protected <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Before
    public void setUp() {
        pafApiStub.start();
    }

    @After
    public void tearDown() throws Exception {
        pafApiStub.stop();
    }
}
