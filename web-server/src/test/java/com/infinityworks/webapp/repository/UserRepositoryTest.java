package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Privilege;
import com.infinityworks.webapp.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.Set;

import static com.infinityworks.webapp.domain.Permission.EDIT_VOTER;
import static com.infinityworks.webapp.domain.Permission.READ_VOTER;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:drop-create.sql",
                        "classpath:constituencies.sql",
                        "classpath:wards.sql",
                        "classpath:users.sql"})
})
public class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void returnsTheUsers() throws Exception {
        List<User> users = userRepository.findAll();

        assertThat(users, hasSize(greaterThan(0)));
    }

    @Test
    public void returnsASpecificUserAndTheirRolesAndPermissions() throws Exception {
        User user = userRepository.findOneByUsername("covs").get();

        Set<Privilege> privileges = user.getPermissions();
        assertThat(privileges, hasItem(new Privilege(EDIT_VOTER)));
        assertThat(privileges, hasItem(new Privilege(READ_VOTER)));
    }
}
