package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import com.infinityworks.webapp.rest.dto.RecordVote;
import com.infinityworks.webapp.rest.dto.SearchElectors;
import com.infinityworks.webapp.service.VoterService;
import com.infinityworks.webapp.service.RecordContactService;
import com.infinityworks.webapp.service.RecordVotedService;
import com.infinityworks.webapp.service.SessionService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/elector")
public class VoterController {
    private final TableBuilder tableBuilder;
    private final DocumentBuilder documentBuilder;
    private final VoterService voterService;
    private final RequestValidator requestValidator;
    private final RecordVotedService recordVotedService;
    private final RecordContactService recordContactService;
    private final SessionService sessionService;
    private final RestErrorHandler errorHandler;

    @Autowired
    public VoterController(@Qualifier("canvass") TableBuilder tableBuilder,
                           @Qualifier("canvass") DocumentBuilder documentBuilder,
                           VoterService voterService,
                           RequestValidator requestValidator,
                           RecordVotedService recordVotedService,
                           RecordContactService recordContactService,
                           SessionService sessionService,
                           RestErrorHandler errorHandler) {
        this.tableBuilder = tableBuilder;
        this.documentBuilder = documentBuilder;
        this.voterService = voterService;
        this.requestValidator = requestValidator;
        this.recordVotedService = recordVotedService;
        this.recordContactService = recordContactService;
        this.sessionService = sessionService;
        this.errorHandler = errorHandler;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET)
    public ResponseEntity<?> searchByAttributes(
            @RequestParam(required = false, name = "wardCode") String wardCode,
            @RequestParam(required = false, name = "firstName") String firstName,
            @RequestParam(required = false, name = "lastName") String lastName,
            @RequestParam(required = false, name = "address") String address,
            @RequestParam(required = false, name = "postCode") String postCode,
            Principal principal) {
        SearchElectors searchRequest = new SearchElectors(firstName, lastName, address, postCode, wardCode);
        return requestValidator.validate(searchRequest)
                .flatMap(request -> sessionService.extractUserFromPrincipal(principal))
                .flatMap(user -> voterService.search(user, searchRequest))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{ern}/contact", method = POST)
    public ResponseEntity<?> recordContact(@PathVariable("ern") String ern,
                                           Principal principal,
                                           @Valid @RequestBody RecordContactRequest contactRequest) throws DocumentException {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> recordContactService.recordContact(user, ern, contactRequest))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = POST, value = "/voted")
    public ResponseEntity<?> recordVote(@RequestBody @Valid RecordVote ern,
                                        Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> recordVotedService.recordVote(user, ern))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/ward/{wardCode}/street/pdf", method = POST)
    public ResponseEntity<?> getPdfOfElectorsByTownStreet(
            @RequestBody @Valid ElectorsByStreetsRequest electorsByStreetsRequest,
            @PathVariable("wardCode") String wardCode,
            Principal principal) throws DocumentException {
        return requestValidator.validate(electorsByStreetsRequest)
                .flatMap(streets -> sessionService.extractUserFromPrincipal(principal))
                .flatMap(user -> voterService.getPdfOfElectorsByStreet(tableBuilder, documentBuilder, electorsByStreetsRequest, wardCode, user))
                .fold(errorHandler::mapToResponseEntity, pdfData -> {
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
                    return new ResponseEntity<>(pdfData.toByteArray(), responseHeaders, HttpStatus.OK);
                });
    }
}
