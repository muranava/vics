package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.ElectorsService;
import com.infinityworks.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ElectorsController {

    private final ElectorsService electorsService;
    private final RestErrorHandler errorHandler;
    private final UserService userService;

    @Autowired
    public ElectorsController(ElectorsService electorsService, RestErrorHandler errorHandler, UserService userService) {
        this.electorsService = electorsService;
        this.errorHandler = errorHandler;
        this.userService = userService;
    }

    @RequestMapping("/ward/{wardCode}/elector")
    public ResponseEntity<?> getElectorsByWard(@PathVariable String wardCode, Principal principal) {
        return userService.extractUserFromPrincipal(principal)
                .flatMap(user -> electorsService.findElectorsByWard(wardCode, user))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
