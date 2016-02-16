package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.AddressService;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.WardService;
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

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Resource to provide information about wards and constituencies
 */
@RestController
@RequestMapping("/ward")
public class WardController {
    private final SessionService userService;
    private final WardService wardService;
    private final RestErrorHandler errorHandler;
    private final AddressService addressService;

    @Autowired
    public WardController(SessionService userService,
                          WardService wardService,
                          RestErrorHandler errorHandler,
                          AddressService addressService) {
        this.userService = userService;
        this.wardService = wardService;
        this.errorHandler = errorHandler;
        this.addressService = addressService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET)
    public ResponseEntity<?> userRestrictedWards(Principal principal) {
        return userService.extractUserFromPrincipal(principal)
                .map(wardService::getByUser)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET, value = "/constituency/{constituencyId}")
    public ResponseEntity<?> wardsByConstituency(Principal principal,
                                                 @PathVariable("constituencyId") String constituencyId) {
        return userService.extractUserFromPrincipal(principal)
                .flatMap(user -> wardService.findByConstituency(UUID.fromString(constituencyId), user))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET, value = "/{wardCode}/street")
    public ResponseEntity<?> streetsByWard(Principal principal,
                                           @PathVariable("wardCode") String wardCode) {
        return userService.extractUserFromPrincipal(principal)
                .flatMap(user -> addressService.getTownStreetsByWardCode(wardCode, user))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = GET, value = "/search")
    public ResponseEntity<?> wardsSearch(
            Principal principal,
            @RequestParam(defaultValue = "10", name = "limit") int limit,
            @RequestParam(required = true, name = "name") @NotEmpty String name) {
        return userService.extractUserFromPrincipal(principal)
                .flatMap(user -> wardService.getAllByName(user, name, limit))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
