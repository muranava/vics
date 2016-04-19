package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geo")
public class GeoController {

    private final GeoService geoService;
    private final RestErrorHandler errorHandler;

    @Autowired
    public GeoController(GeoService geoService, RestErrorHandler errorHandler) {
        this.geoService = geoService;
        this.errorHandler = errorHandler;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/addresslookup")
    public ResponseEntity<?> reverseGeolocate(@RequestParam("q") String searchTerm) {
        return geoService
                .reverseGeolocateAddress(searchTerm)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
