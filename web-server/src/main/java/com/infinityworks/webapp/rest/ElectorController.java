package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.ErrorHandler;
import com.infinityworks.webapp.rest.dto.ElectorPreviewRequest;
import com.infinityworks.webapp.rest.dto.PrintElectorsRequest;
import com.infinityworks.webapp.service.PreviewService;
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
    private final PreviewService previewService;
    private final PrintService printService;
    private final ErrorHandler errorHandler;

    @Autowired
    public ElectorController(RequestValidator requestValidator,
                             PreviewService previewService,
                             PrintService printService,
                             ErrorHandler errorHandler) {
        this.requestValidator = requestValidator;
        this.previewService = previewService;
        this.printService = printService;
        this.errorHandler = errorHandler;
    }

    @RequestMapping(value = "/preview", method = POST)
    public ResponseEntity<?> previewVotersByWard(@RequestBody ElectorPreviewRequest electorRequest) {
        return requestValidator
                .validate(electorRequest)
                .flatMap(previewService::previewElectorsByWards)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(value = "/print")
    public ResponseEntity<?> printElectors(@RequestBody PrintElectorsRequest printRequest) {
        return requestValidator
                .validate(printRequest)
                .flatMap(printService::printElectors)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(value = "/print/enriched")
    public ResponseEntity<?> printEnrichedElectors(@RequestBody PrintElectorsRequest printRequest) {
        return requestValidator
                .validate(printRequest)
                .flatMap(printService::printEnrichedElectors)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
