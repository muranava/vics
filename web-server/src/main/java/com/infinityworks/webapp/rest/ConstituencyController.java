package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.ConstituencyService;
import com.infinityworks.webapp.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/constituency")
public class ConstituencyController {
    private final ConstituencyService constituencyService;
    private final RestErrorHandler errorHandler;
    private final SessionService userService;

    @Autowired
    public ConstituencyController(ConstituencyService constituencyService,
                                  RestErrorHandler errorHandler,
                                  SessionService userService) {
        this.constituencyService = constituencyService;
        this.errorHandler = errorHandler;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET)
    public ResponseEntity<?> visibleConstituencies(Principal principal) {
        return userService.extractUserFromPrincipal(principal)
                .map(constituencyService::getVisibleConstituenciesByUser)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
