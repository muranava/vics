package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.CurrentUser;
import com.infinityworks.webapp.domain.Role;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.dto.AuthenticationToken;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import com.infinityworks.webapp.rest.dto.UpdateUserRequest;
import com.infinityworks.webapp.security.SecurityUtils;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final SessionService sessionService;
    private final RestErrorHandler errorHandler;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final RequestValidator requestValidator;

    @Autowired
    public UserController(UserService userService,
                          RestErrorHandler errorHandler,
                          UserDetailsService userDetailsService,
                          SessionService sessionService,
                          AuthenticationManager authenticationManager,
                          RequestValidator requestValidator) {
        this.userService = userService;
        this.errorHandler = errorHandler;
        this.userDetailsService = userDetailsService;
        this.sessionService = sessionService;
        this.authenticationManager = authenticationManager;
        this.requestValidator = requestValidator;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/current", method = GET)
    public ResponseEntity<?> currentUser(Principal userPrincipal) {
        return sessionService.extractUserFromPrincipal(userPrincipal)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id, Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> userService.delete(user, id))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody UpdateUserRequest updateRequest, Principal principal) {
        return requestValidator.validate(updateRequest)
                .flatMap(request -> sessionService.extractUserFromPrincipal(principal))
                .flatMap(user -> userService.update(user, id, updateRequest))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = POST)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest, Principal principal) {
        return requestValidator.validate(createUserRequest)
                .flatMap(request -> sessionService.extractUserFromPrincipal(principal))
                .flatMap(user -> userService.create(user, createUserRequest))
                .fold(errorHandler::mapToResponseEntity,
                        newUser -> {
                            log.debug("Created user={}", newUser);
                            return ResponseEntity.status(CREATED).body(newUser);
                        });
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = GET)
    public ResponseEntity<?> allUsers() {
        return userService.getAll()
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = GET, value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable(value = "id") UUID id, Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> userService.getByID(user, id))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(value = "/login", method = POST)
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
        }).fold(errorHandler::mapToResponseEntity, token -> {
            log.info("User={} logged in", token);
            return ResponseEntity.ok(token);
        });
    }

    @RequestMapping(value = "/logout", method = POST)
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/login/test", method = GET)
    public ResponseEntity<?> test(Principal userPrincipal, @RequestParam(value = "role") String role) {
        return Role.of(role).flatMap(r -> {
            Object principal = ((UsernamePasswordAuthenticationToken) userPrincipal).getPrincipal();
            CurrentUser user = (CurrentUser) principal;
            if (Role.hasPermission(user.getRole(), r)) {
                return Try.success(user);
            } else {
                return Try.failure(new AccessDeniedException("Forbidden"));
            }
        }).fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
