package com.infinityworks.webapp.security;

import com.infinityworks.webapp.domain.PasswordResetToken;
import com.infinityworks.webapp.repository.PasswordResetTokenRepository;
import com.infinityworks.webapp.repository.RepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:drop-create.sql",
                        "classpath:constituencies.sql",
                        "classpath:wards.sql",
                        "classpath:users.sql",
                        "classpath:record-contact-logs.sql",
                        "classpath:password-reset-token.sql"
                })
})
public class PasswordResetTokenGeneratorTest extends RepositoryTest {

    @Autowired
    private PasswordResetTokenRepository repository;

    @Test
    public void returnsTheTokens() throws Exception {
        List<PasswordResetToken> users = repository.findAll();

        assertThat(users, hasSize(greaterThan(0)));
    }

    @Test
    public void deletesTheExistingTokenByUsername() {
        String username = "stein.fletcher@voteleave.uk";
        Optional<PasswordResetToken> oneByUserUsername = repository.findOneByUserUsername(username);
        assertTrue(oneByUserUsername.isPresent());

        repository.delete(oneByUserUsername.get());
        Optional<PasswordResetToken> oneByUserUsernamePostDelete = repository.findOneByUserUsername(username);
        assertFalse(oneByUserUsernamePostDelete.isPresent());
    }
}