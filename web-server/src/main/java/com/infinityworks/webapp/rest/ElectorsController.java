package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.ElectorsService;
import com.infinityworks.webapp.service.SessionService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/elector")
public class ElectorsController {

    private final ElectorsService electorsService;
    private final RequestValidator requestValidator;
    private final SessionService sessionService;
    private final RestErrorHandler errorHandler;

    @Autowired
    public ElectorsController(ElectorsService electorsService,
                              RequestValidator requestValidator,
                              SessionService sessionService, RestErrorHandler errorHandler) {
        this.electorsService = electorsService;
        this.requestValidator = requestValidator;
        this.sessionService = sessionService;
        this.errorHandler = errorHandler;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{ern}", method = GET)
    public ResponseEntity<?> getElectorByErn(@PathVariable("ern") String ern,
                                             Principal principal) throws DocumentException {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> electorsService.electorByErn(ern))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{ern}/contact", method = POST)
    public ResponseEntity<?> recordContact(@PathVariable("ern") String ern,
                                           Principal principal,
                                           @Valid @RequestBody RecordContactRequest contactRequest) throws DocumentException {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> electorsService.recordContact(user, ern, contactRequest))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/ward/{wardCode}/street/pdf", method = POST, produces = "application/pdf")
    public ResponseEntity<byte[]> getPdfOfElectorsByTownStreet(
            @RequestBody @Valid TownStreets townStreets,
            @PathVariable("wardCode") String wardCode,
            Principal principal) throws DocumentException {
        return requestValidator.validate(townStreets)
                .flatMap(streets -> sessionService.extractUserFromPrincipal(principal))
                .flatMap(user -> electorsService.electorsByStreets(townStreets, wardCode, user))
                .fold(error -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[]{}),
                      pdfData -> ResponseEntity.ok(pdfData.toByteArray()));
    }
}
