package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.ErrorHandler;
import com.infinityworks.webapp.rest.dto.ElectorsByWardAndConstituencyRequest;
import com.infinityworks.webapp.rest.dto.ElectorsByWardsRequest;
import com.infinityworks.webapp.service.PrintService;
import com.infinityworks.webapp.service.WardService;
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
    private final WardService wardService;
    private final ErrorHandler errorHandler;

    @Autowired
    public ElectorController(RequestValidator requestValidator,
                             PrintService printService,
                             WardService wardService,
                             ErrorHandler errorHandler) {
        this.requestValidator = requestValidator;
        this.printService = printService;
        this.wardService = wardService;
        this.errorHandler = errorHandler;
    }

    @RequestMapping(value = "/preview", method = POST)
    public ResponseEntity<?> getVotersByWard(@RequestBody ElectorsByWardAndConstituencyRequest electorRequest) {
        return requestValidator
                .validate(electorRequest)
                .flatMap(wardService::findElectorsByWard)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> printElectorsWithPafAddresses(@RequestBody ElectorsByWardsRequest printRequest) {
        return requestValidator
                .validate(printRequest)
                .flatMap(printService::findPafEnrichedElectors)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(value = "/local", method = POST)
    public ResponseEntity<?> printElectorsLocal(@RequestBody ElectorsByWardsRequest printRequest) {
        return requestValidator
                .validate(printRequest)
                .flatMap(printService::findLocalElectors)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
