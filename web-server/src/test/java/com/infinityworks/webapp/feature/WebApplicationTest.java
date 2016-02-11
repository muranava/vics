package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.Application;
import com.infinityworks.webapp.config.Config;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.security.SecurityConfig;
import com.infinityworks.webapp.service.UserService;
import com.infinityworks.webapp.testsupport.stub.PafServerStub;
import org.junit.After;
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
    protected final PafServerStub pafApiStub = new PafServerStub();

    @Autowired
    protected WebApplicationContext applicationContext;

    @Autowired
    protected Filter springSecurityFilterChain;

    protected User admin() {
        UserService userService = getBean(UserService.class);
        return userService.getByEmail("admin").get();
    }

    protected User covs() {
        UserService userService = getBean(UserService.class);
        return userService.getByEmail("covs").get();
    }

    protected <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @After
    public void tearDown() {
        pafApiStub.stop();
    }
}
