package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.email.ImmutableEmailResponse;
import com.infinityworks.webapp.config.AppProperties;
import com.infinityworks.webapp.domain.PasswordResetToken;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.repository.PasswordResetTokenRepository;
import com.infinityworks.webapp.rest.dto.ImmutablePasswordResetRequest;
import com.infinityworks.webapp.rest.dto.PasswordResetRequest;
import com.infinityworks.webapp.rest.dto.ResetPasswordToken;
import com.infinityworks.webapp.security.PasswordResetTokenGenerator;
import org.junit.Before;
import org.junit.Test;

import static com.infinityworks.webapp.testsupport.Fixtures.token;
import static com.infinityworks.webapp.testsupport.matcher.TryFailureMatcher.isFailure;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class PasswordResetServiceTest {

    private PasswordResetService underTest;
    private UserService userService;
    private PasswordResetNotifier passwordResetNotifier;
    private PasswordResetTokenGenerator passwordResetTokenGenerator;

    @Before
    public void setUp() throws Exception {
        AppProperties props = mock(AppProperties.class);
        when(props.getPasswordResetExpirationMins()).thenReturn(20);
        passwordResetNotifier = mock(PasswordResetNotifier.class);
        PasswordResetTokenRepository passwordResetTokenRepository = mock(PasswordResetTokenRepository.class);
        passwordResetTokenGenerator = mock(PasswordResetTokenGenerator.class);
        userService = mock(UserService.class);
        underTest = new PasswordResetService(userService, passwordResetNotifier, passwordResetTokenGenerator, passwordResetTokenRepository);
    }

    @Test
    public void requestsPasswordReset() throws Exception {
        PasswordResetToken token = token();

        given(passwordResetTokenGenerator.generateToken(token.getUser())).willReturn(token);
        given(passwordResetNotifier.sendPasswordResetNotification(token.getUser(), token.getToken())).willReturn(Try.success(ImmutableEmailResponse.builder().withMessage("message").build()));
        given(userService.getByUsername(token.getUser().getUsername())).willReturn(Try.success(token.getUser()));
        PasswordResetRequest request = ImmutablePasswordResetRequest.builder().withUsername(token.getUser().getUsername()).build();

        Try<ResetPasswordToken> uname = underTest.resetPassword(request);

        assertThat(uname.get().token(), equalTo(token.getToken()));
        verify(passwordResetNotifier, times(1)).sendPasswordResetNotification(token.getUser(), token.getToken());
    }

    @Test
    public void returnsNotFoundErrorIfUserDoesNotExist() throws Exception {
        String username = "idontexist@nothing.me";
        given(userService.getByUsername(username)).willReturn(Try.failure(new NotFoundFailure("No user with name")));
        PasswordResetRequest request = ImmutablePasswordResetRequest.builder().withUsername(username).build();

        Try<ResetPasswordToken> uname = underTest.resetPassword(request);

        assertThat(uname, isFailure(instanceOf(NotFoundFailure.class)));
        verifyZeroInteractions(passwordResetNotifier);
    }
}
