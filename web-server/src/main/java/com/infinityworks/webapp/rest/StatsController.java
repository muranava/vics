package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private final StatsService statsService;
    private final SessionService sessionService;
    private final RestErrorHandler restErrorHandler;

    @Autowired
    public StatsController(StatsService statsService, SessionService sessionService, RestErrorHandler restErrorHandler) {
        this.statsService = statsService;
        this.sessionService = sessionService;
        this.restErrorHandler = restErrorHandler;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/topcanvassers", method = RequestMethod.GET)
    public ResponseEntity<?> topCanvassers() {
        return ResponseEntity.ok(statsService.topCanvassers());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/topconstituencies", method = RequestMethod.GET)
    public ResponseEntity<?> mostCanvassedConstituencies() {
        return ResponseEntity.ok(statsService.mostCanvassedConstituencies());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/topwards", method = RequestMethod.GET)
    public ResponseEntity<?> mostCanavassedWards() {
        return ResponseEntity.ok(statsService.mostCanvassedWards());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/ward/{wardCode}/weekly", method = RequestMethod.GET)
    public ResponseEntity<?> recordContactByDateAndWard(@PathVariable("wardCode") String wardCode) {
        return ResponseEntity.ok(statsService.countRecordContactsByDateAndWard(wardCode));
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/constituency/{constituencyCode}/weekly", method = RequestMethod.GET)
    public ResponseEntity<?> recordContactByDateAndConstituency(@PathVariable("constituencyCode") String constituencyCode) {
        return ResponseEntity.ok(statsService.countRecordContactsByDateAndConstituency(constituencyCode));
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> allStats() {
        return ResponseEntity.ok(statsService.allStats());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> countUsersByRegion(Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(statsService::countUsersByRegion)
                .fold(restErrorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, value = "/ward/{wardCode}")
    public ResponseEntity<?> wardStatsFromPaf(@PathVariable("wardCode") String wardCode, Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> statsService.wardStats(user, wardCode))
                .fold(restErrorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, value = "/constituency/{constituencyCode}")
    public ResponseEntity<?> constituencyStatsFromPaf(@PathVariable("constituencyCode") String constituencyCode, Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> statsService.constituencyStats(user, constituencyCode))
                .fold(restErrorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
