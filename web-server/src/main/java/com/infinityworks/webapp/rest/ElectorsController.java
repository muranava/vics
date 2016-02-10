package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.ElectorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElectorsController {

    private final ElectorsService electorsService;
    private final RestErrorHandler errorHandler;

    @Autowired
    public ElectorsController(ElectorsService electorsService, RestErrorHandler errorHandler) {
        this.electorsService = electorsService;
        this.errorHandler = errorHandler;
    }

    @RequestMapping("/ward/{wardCode}/elector")
    public ResponseEntity<?> getElectorsByWard(@PathVariable String wardCode) {
        return electorsService
                .findElectorsByWard(wardCode)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
