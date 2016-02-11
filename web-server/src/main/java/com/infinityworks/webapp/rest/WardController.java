package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.AddressService;
import com.infinityworks.webapp.service.UserService;
import com.infinityworks.webapp.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Resource to provide information about wards and constituencies
 */
@RestController
public class WardController {
    private final UserService userService;
    private final WardService wardService;
    private final RestErrorHandler errorHandler;
    private final AddressService addressService;

    @Autowired
    public WardController(UserService userService,
                          WardService wardService,
                          RestErrorHandler errorHandler,
                          AddressService addressService) {
        this.userService = userService;
        this.wardService = wardService;
        this.errorHandler = errorHandler;
        this.addressService = addressService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET, value = "/ward")
    public ResponseEntity<?> allWards(Principal principal) {
        return userService.extractUserFromPrincipal(principal)
                .map(wardService::getByUser)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET, value = "/ward/{wardCode}/street")
    public ResponseEntity<?> streetsByWard(Principal principal, @PathVariable("wardCode") String wardCode) {
        return userService.extractUserFromPrincipal(principal)
                .flatMap(user -> addressService.getTownStreetsByWardCode(wardCode, user))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
