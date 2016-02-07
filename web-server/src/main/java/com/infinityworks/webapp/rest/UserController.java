package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.rest.dto.AuthenticationToken;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import com.infinityworks.webapp.rest.dto.LoginRequest;
import com.infinityworks.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AuthenticationToken authenticate(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        UserDetails details = this.userDetailsService.loadUserByUsername(loginRequest.getUsername());
        List<String> roles = details.getAuthorities().stream()
                .map(Object::toString)
                .collect(toList());
        return new AuthenticationToken(details.getUsername(), roles, session.getId());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/login/test", method = { RequestMethod.GET })
    public void test(HttpServletRequest request) {
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public User createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = userService.create(createUserRequest);
        log.info("Created user: " + user);
        return user;
    }
}
