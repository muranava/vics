package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.dto.AssociateUserConstituency;
import com.infinityworks.webapp.service.ConstituencyService;
import com.infinityworks.webapp.service.SessionService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET, value = "/search", produces = "application/json")
    public ResponseEntity<?> constituencySearch(
            Principal principal,
            @RequestParam(defaultValue = "10", name = "limit") int limit,
            @RequestParam(name = "name") @NotEmpty String name) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> constituencyService.constituenciesByName(user, name, limit))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = POST, value = "/{constituencyID}/user/{userID}")
    public ResponseEntity<?> addUserAssociation(
            Principal principal,
            @PathVariable("constituencyID") UUID constituencyID,
            @PathVariable("userID") UUID userID) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> constituencyService.associateToUser(user, constituencyID, userID))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = POST, value = "/associate")
    public ResponseEntity<?> addUserAssociationByUsernameAndConstituencyCode(
            Principal principal,
            @RequestBody AssociateUserConstituency association) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> constituencyService.associateToUserByUsername(user, association.getConstituencyCode(),
                        association.getUsername()))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = DELETE, value = "/{constituencyID}/user/{userID}")
    public ResponseEntity<?> removeUserAssociation(
            Principal principal,
            @PathVariable("constituencyID") UUID constituencyID,
            @PathVariable("userID") UUID userID) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> constituencyService.removeUserAssociation(user, constituencyID, userID))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
