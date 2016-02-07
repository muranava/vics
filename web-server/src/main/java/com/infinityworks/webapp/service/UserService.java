package com.infinityworks.webapp.service;

import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(UUID id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findOneByUsername(email);
    }

    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("username"));
    }

    public User create(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setRole(request.getRole());
        return userRepository.save(user);
    }
}
