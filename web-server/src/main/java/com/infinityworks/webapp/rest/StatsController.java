package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/topcanvassers", method = RequestMethod.GET)
    public ResponseEntity<?> topCanvassers() {
        return ResponseEntity.ok(statsService.topCanvassersStats());
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
}
