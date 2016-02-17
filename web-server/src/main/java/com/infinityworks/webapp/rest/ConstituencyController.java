package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.validation.IsUUID;
import com.infinityworks.webapp.service.ConstituencyService;
import com.infinityworks.webapp.service.SessionService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/constituency")
public class ConstituencyController {
    private final ConstituencyService constituencyService;
    private final RestErrorHandler errorHandler;
    private final SessionService sessionService;

    @Autowired
    public ConstituencyController(ConstituencyService constituencyService,
                                  RestErrorHandler errorHandler,
                                  SessionService sessionService) {
        this.constituencyService = constituencyService;
        this.errorHandler = errorHandler;
        this.sessionService = sessionService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET)
    public ResponseEntity<?> visibleConstituencies(Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .map(constituencyService::getVisibleConstituenciesByUserWithWardContext)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = GET, value = "/search", produces = "application/json")
    public ResponseEntity<?> constituencySearch(
            Principal principal,
            @RequestParam(defaultValue = "10", name = "limit") int limit,
            @RequestParam(required = true, name = "name") @NotEmpty String name) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> constituencyService.constituenciesByName(user, name, limit))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = POST, value = "/{constituencyID}/user/{userID}")
    public ResponseEntity<?> addUserAssociation(
            Principal principal,
            @PathVariable("constituencyID") @IsUUID String constituencyID,
            @PathVariable("userID") @IsUUID String userID) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> constituencyService.associateToUser(user, UUID.fromString(constituencyID), UUID.fromString(userID)))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = DELETE, value = "/{constituencyID}/user/{userID}")
    public ResponseEntity<?> removeUserAssociation(
            Principal principal,
            @PathVariable("constituencyID") @IsUUID String constituencyID,
            @PathVariable("userID") @IsUUID String userID) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> constituencyService.removeUserAssociation(user, UUID.fromString(constituencyID), UUID.fromString(userID)))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}