package com.infinityworks.webapp.rest;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.domain.CurrentUser;
import com.infinityworks.webapp.domain.Role;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import com.infinityworks.webapp.rest.dto.UpdateUserRequest;
import com.infinityworks.webapp.security.SecurityUtils;
import com.infinityworks.webapp.service.LoginService;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final LoginService loginService;
    private final SessionService sessionService;
    private final RestErrorHandler errorHandler;
    private final RequestValidator requestValidator;

    @Autowired
    public UserController(UserService userService,
                          RestErrorHandler errorHandler,
                          SessionService sessionService,
                          RequestValidator requestValidator,
                          LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
        this.errorHandler = errorHandler;
        this.sessionService = sessionService;
        this.requestValidator = requestValidator;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/current", method = GET)
    public ResponseEntity<?> currentUser(Principal userPrincipal) {
        return sessionService.extractUserFromPrincipal(userPrincipal)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id, Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> userService.delete(user, id))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody UpdateUserRequest updateRequest, Principal principal) {
        return requestValidator.validate(updateRequest)
                .flatMap(request -> sessionService.extractUserFromPrincipal(principal))
                .flatMap(user -> userService.update(user, id, updateRequest))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = POST)
    public ResponseEntity<?> create(@RequestBody CreateUserRequest createUserRequest, Principal principal) {
        return requestValidator.validate(createUserRequest)
                .flatMap(request -> sessionService.extractUserFromPrincipal(principal))
                .flatMap((User user) -> userService.create(user, createUserRequest))
                .fold(errorHandler::mapToResponseEntity,
                        newUser -> {
                            log.debug("Created user={}", newUser);
                            return ResponseEntity.status(CREATED).body(newUser);
                        });
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET)
    public ResponseEntity<?> allUsers(Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(userService::getAll)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET, value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable(value = "id") UUID id, Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> userService.getByID(user, id))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<?> authenticate(@RequestHeader("Authorization") String authorization,
                                          HttpServletRequest request) {
        return SecurityUtils.credentialsFromAuthHeader(authorization)
                .flatMap(credentials -> loginService.login(credentials, request.getSession(true)))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);

    }

    @RequestMapping(value = "/logout", method = POST)
    public void logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/login/test", method = GET)
    public ResponseEntity<?> testLoggedIn(Principal userPrincipal, @RequestParam(value = "role") String role) {
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
