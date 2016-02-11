package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.ElectorsService;
import com.infinityworks.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("elector")
public class ElectorsController {

    private final ElectorsService electorsService;
    private final RestErrorHandler errorHandler;
    private final RequestValidator requestValidator;
    private final UserService userService;

    @Autowired
    public ElectorsController(ElectorsService electorsService,
                              RestErrorHandler errorHandler,
                              RequestValidator requestValidator,
                              UserService userService) {
        this.electorsService = electorsService;
        this.errorHandler = errorHandler;
        this.requestValidator = requestValidator;
        this.userService = userService;
    }

    @RequestMapping(value = "/ward/{wardCode}/street", method = POST)
    public ResponseEntity<?> getElectorsByTownStreet(@RequestBody TownStreets townStreets,
                                                     @PathVariable("wardCode") String wardCode,
                                                     Principal principal) {
        return requestValidator.validate(townStreets)
                .flatMap(streets -> userService.extractUserFromPrincipal(principal))
                .flatMap(user -> electorsService.findElectorsByStreet(townStreets, wardCode, user))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
