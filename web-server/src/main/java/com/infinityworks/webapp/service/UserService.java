package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.CurrentUser;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.UserSessionFailure;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findOneByUsername(email);
    }

    public Try<Collection<User>> getAll() {
        return Try.of(() -> userRepository.findAll(new Sort("username")));
    }

    public Try<User> extractUserFromPrincipal(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            Object sessionPrincipal = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            if (sessionPrincipal instanceof CurrentUser) {
                CurrentUser currentUser = (CurrentUser) sessionPrincipal;
                return Try.success(currentUser.getUser());
            }
        }
        return Try.failure(new UserSessionFailure("Incorrect authentication configuration. Could not extract user from session"));
    }

    @Transactional
    public Try<User> create(CreateUserRequest request) {
        if (!Objects.equals(request.getPassword(), request.getPasswordRepeated())) {
            return Try.failure(new BadCredentialsException("Passwords do not match"));
        }

        User user = new User();
        user.setUsername(request.getEmail());
        user.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setRole(request.getRole());
        User newUser = userRepository.save(user);
        return Try.success(newUser);
    }
}
