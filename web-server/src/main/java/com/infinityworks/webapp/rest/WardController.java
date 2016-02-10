package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.UserService;
import com.infinityworks.webapp.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Resource to provide information about wards and constituencies
 */
@RestController
public class WardController {
    private final UserService userService;
    private final WardService wardService;
    private final RestErrorHandler errorHandler;

    @Autowired
    public WardController(UserService userService,
                          WardService wardService,
                          RestErrorHandler errorHandler) {
        this.userService = userService;
        this.wardService = wardService;
        this.errorHandler = errorHandler;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET, value = "/ward")
    public ResponseEntity<?> allWards(Principal principal) {
        return userService.extractUserFromPrincipal(principal)
                .map(wardService::getByUser)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
