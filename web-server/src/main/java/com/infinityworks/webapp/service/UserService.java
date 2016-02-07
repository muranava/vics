package com.infinityworks.webapp.service;

import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
        return userRepository.findOneByEmail(email);
    }

    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }

    public User create(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setRole(request.getRole());
        return userRepository.save(user);
    }
    public static void main(String... args) {
        System.out.println(BCrypt.hashpw("stein", "$2a$12$.vnOLyiNeVjJJTbT4ebt6u"));
    }
}
