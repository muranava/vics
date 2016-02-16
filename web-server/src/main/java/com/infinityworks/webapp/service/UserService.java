package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.BadRequestFailure;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
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

    @Transactional
    public Try<Void> delete(User user, UUID userToDelete) {
        if (!user.isAdmin()) {
            log.error("Non admin tried to delete a user!. User={}, userToDelete={}", user, userToDelete);
            return Try.failure(new NotAuthorizedFailure("Not authorized"));
        }
        if (userRepository.findOne(userToDelete) == null) {
            log.warn("Attempt to delete non existent user. User={}, userToDelete={}", user, userToDelete);
            return Try.failure(new NotFoundFailure("No user with ID=" + userToDelete));
        }
        userRepository.delete(userToDelete);
        log.debug("Deleted user={}", userToDelete);
        return Try.success(null);
    }

    @Transactional
    public Try<User> create(User user, CreateUserRequest request) {
        if (!user.isAdmin()) {
            log.error("Non admin tried to create user!. User={}, request={}", user, request);
            return Try.failure(new NotAuthorizedFailure("Not authorized"));
        }

        if (!Objects.equals(request.getPassword(), request.getRepeatPassword())) {
            return Try.failure(new BadRequestFailure("Passwords do not match"));
        }

        Optional<User> oneByUsername = userRepository.findOneByUsername(request.getEmail());
        if (oneByUsername.isPresent()) {
            return Try.failure(new BadRequestFailure("User already exists"));
        }

        // TODO add constituencies / wards
        User newUser = new User();
        newUser.setUsername(request.getEmail());
        newUser.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        newUser.setRole(request.getRole());
        newUser.setWriteAccess(request.getWriteAccess());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setWriteAccess(request.getWriteAccess());
        User savedUser = userRepository.save(newUser);
        return Try.success(savedUser);
    }
}
