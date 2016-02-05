package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.ErrorHandler;
import com.infinityworks.webapp.rest.dto.ElectorsByWardsRequest;
import com.infinityworks.webapp.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/elector")
public class ElectorController {

    private final RequestValidator requestValidator;
    private final PrintService printService;
    private final ErrorHandler errorHandler;

    @Autowired
    public ElectorController(RequestValidator requestValidator,
                             PrintService printService,
                             ErrorHandler errorHandler) {
        this.requestValidator = requestValidator;
        this.printService = printService;
        this.errorHandler = errorHandler;
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> findElectorsWithPafAddresses(@RequestBody ElectorsByWardsRequest request) {
        return requestValidator
                .validate(request)
                .flatMap(printService::findPafEnrichedElectors)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(value = "/local", method = POST)
    public ResponseEntity<?> findElectorsLocal(@RequestBody ElectorsByWardsRequest request) {
        return requestValidator
                .validate(request)
                .flatMap(printService::findLocalElectors)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
