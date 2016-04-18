package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.dto.AddressLookupRequest;
import com.infinityworks.webapp.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

    @RequestMapping(value = "/addresslookup", method = POST)
    public ResponseEntity<?> reverseGeolocate(@RequestBody AddressLookupRequest request) {
        return geoService
                .reverseGeolocateAddress(request)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
