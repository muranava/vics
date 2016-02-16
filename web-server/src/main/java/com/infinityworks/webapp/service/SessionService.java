package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.CurrentUser;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.UserSessionFailure;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class SessionService {
    public Try<User> extractUserFromPrincipal(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            Object sessionPrincipal = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            if (sessionPrincipal instanceof org.springframework.security.core.userdetails.User) {
                CurrentUser currentUser = (CurrentUser) sessionPrincipal;
                return Try.success(currentUser.getUser());
            }
        }
        return Try.failure(new UserSessionFailure("Incorrect authentication configuration. Could not extract user from session"));
    }
}
