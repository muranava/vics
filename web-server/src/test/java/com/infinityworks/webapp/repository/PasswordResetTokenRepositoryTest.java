package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.PasswordResetToken;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.service.RemoveExpiredPasswordResetTokensService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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
public class PasswordResetTokenRepositoryTest extends RepositoryTest {

    @Autowired private PasswordResetTokenRepository repository;
    @Autowired private UserRepository userRepository;
    @Autowired private RemoveExpiredPasswordResetTokensService removeTokensService;

    @Test
    public void removesExpiredTokens() throws Exception {
        repository.deleteAll();

        insertExpiredTokens();
        insertNonExpiredTokens();

        assertThat(repository.count(), equalTo(4L));

        removeTokensService.removeExpiredTokens();

        assertThat(repository.count(), equalTo(2L));
    }

    private void insertExpiredTokens() {
        User user1 = userRepository.findOneByUsername("stein.fletcher@voteleave.uk").get();
        User user2 = userRepository.findOneByUsername("cov@south.cov").get();

        PasswordResetToken resetToken1 = new PasswordResetToken();
        resetToken1.setToken("expiredToken1");
        resetToken1.setUser(user1);
        resetToken1.setExpires(LocalDateTime.now().minusMinutes(1));

        PasswordResetToken resetToken2 = new PasswordResetToken();
        resetToken2.setToken("expiredToken2");
        resetToken2.setUser(user2);
        resetToken2.setExpires(LocalDateTime.now().minusMinutes(50));

        repository.save(resetToken1);
        repository.save(resetToken2);
    }

    private void insertNonExpiredTokens() {
        User user1 = userRepository.findOneByUsername("kalderon@voteleave.uk").get();
        User user2 = userRepository.findOneByUsername("avamcall@voteleave.uk").get();

        PasswordResetToken resetToken1 = new PasswordResetToken();
        resetToken1.setToken("validToken1");
        resetToken1.setUser(user1);
        resetToken1.setExpires(LocalDateTime.now().plusMinutes(2));

        PasswordResetToken resetToken2 = new PasswordResetToken();
        resetToken2.setToken("validToken2");
        resetToken2.setUser(user2);
        resetToken2.setExpires(LocalDateTime.now().plusMinutes(10));

        repository.save(resetToken1);
        repository.save(resetToken2);
    }
}
