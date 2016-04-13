package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.PasswordResetToken;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.repository.PasswordResetTokenRepository;
import com.infinityworks.webapp.rest.dto.ImmutableResetPasswordToken;
import com.infinityworks.webapp.rest.dto.PasswordResetRequest;
import com.infinityworks.webapp.rest.dto.ResetPasswordToken;
import com.infinityworks.webapp.security.PasswordResetTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to handle password reset.  The reset password flow is as follows
 *
 * 1. User clicks 'Forgotten password' which triggers a POST request and invocation of this service.
 * 2. This service
 *    i)   generates and emails a token to the user
 *    ii)  stores the token in the database
 * 3. The user navigates to a password reset URL (which contains the token) by clicking the link in their email.
 *    This POSTs the token to the server and the server returns the new password in the response.
 * 4. The token gets deleted in the database (or deleted by a scheduled operation at some point if the token has expired).
 */
@Service
public class PasswordResetService {

    private final UserService userService;
    private final PasswordResetNotifier passwordResetNotifier;
    private final PasswordResetTokenGenerator passwordResetTokenGenerator;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public PasswordResetService(UserService userService,
                                PasswordResetNotifier passwordResetNotifier,
                                PasswordResetTokenGenerator passwordResetToken,
                                PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userService = userService;
        this.passwordResetNotifier = passwordResetNotifier;
        this.passwordResetTokenGenerator = passwordResetToken;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public Try<ResetPasswordToken> resetPassword(PasswordResetRequest request) {
        return userService
                .getByUsername(request.username())
                .flatMap(user -> {
                    PasswordResetToken resetToken = passwordResetTokenGenerator.generateToken(user);
                    return passwordResetNotifier
                            .sendPasswordResetNotification(user, resetToken.getToken())
                            .map(emailResponse -> {
                                String token = persistToken(user, resetToken);
                                return ImmutableResetPasswordToken.builder().withToken(token).build();
                            });
                });
    }

    /**
     * Persist the user reset password token.  We also delete the users existing token if it exists
     */
    private String persistToken(User user, PasswordResetToken resetToken) {
        Optional<PasswordResetToken> existingTokenForUser = passwordResetTokenRepository.findOneByUserUsername(user.getUsername());
        if (existingTokenForUser.isPresent()) {
            passwordResetTokenRepository.delete(existingTokenForUser.get());
        }
        PasswordResetToken savedToken = passwordResetTokenRepository.save(resetToken);
        return savedToken.getToken();
    }
}
