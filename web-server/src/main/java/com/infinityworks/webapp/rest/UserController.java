package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.ErrorHandler;
import com.infinityworks.webapp.rest.dto.AuthenticationToken;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import com.infinityworks.webapp.security.SecurityUtils;
import com.infinityworks.webapp.service.UserService;
import org.apache.http.auth.AUTH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ErrorHandler errorHandler;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService,
                          ErrorHandler errorHandler,
                          UserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.errorHandler = errorHandler;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    // TODO rework and test
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        return SecurityUtils.credentialsFromAuthHeader(authorization).flatMap(credentials -> {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());

            return Try.of(() -> this.authenticationManager.authenticate(token)).map(authentication -> {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

                UserDetails details = this.userDetailsService.loadUserByUsername(credentials.getUsername());
                List<String> roles = details.getAuthorities().stream()
                        .map(Object::toString)
                        .collect(toList());
                return new AuthenticationToken(details.getUsername(), roles, session.getId());
            });
        }).fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    // FIXME check we don't leak details to UI
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/login/test", method = {RequestMethod.GET})
    public Principal test(HttpServletRequest request) {
        return request.getUserPrincipal();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public User createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = userService.create(createUserRequest);
        log.info("Created user: " + user);
        return user;
    }
}
