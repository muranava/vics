package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.domain.Ern;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import com.infinityworks.webapp.rest.dto.RecordVoteRequest;
import com.infinityworks.webapp.rest.dto.SearchElectors;
import com.infinityworks.webapp.service.*;
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
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/elector")
public class VoterController {
    private final TableBuilder tableBuilder;
    private final DocumentBuilder documentBuilder;
    private final VoterService voterService;
    private final LabelService labelService;
    private final RequestValidator requestValidator;
    private final RecordVotedService recordVotedService;
    private final RecordContactService contactService;
    private final SessionService sessionService;
    private final RestErrorHandler errorHandler;

    @Autowired
    public VoterController(@Qualifier("canvass") TableBuilder tableBuilder,
                           @Qualifier("canvass") DocumentBuilder documentBuilder,
                           VoterService voterService,
                           LabelService labelService,
                           RequestValidator requestValidator,
                           RecordVotedService recordVotedService,
                           RecordContactService contactService,
                           SessionService sessionService,
                           RestErrorHandler errorHandler) {
        this.tableBuilder = tableBuilder;
        this.documentBuilder = documentBuilder;
        this.voterService = voterService;
        this.labelService = labelService;
        this.requestValidator = requestValidator;
        this.recordVotedService = recordVotedService;
        this.contactService = contactService;
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
    public ResponseEntity<?> recordContact(Principal principal,
                                           @PathVariable("ern") Ern ern,
                                           @Valid @RequestBody RecordContactRequest recordContactRequest) throws DocumentException {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> contactService.recordContact(user, ern, recordContactRequest))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{ern}/contact/{contactId}/localId/{localId}", method = DELETE)
    public ResponseEntity<?> deleteContact(Principal principal,
                                           @PathVariable("ern") Ern ern,
                                           @PathVariable("contactId") UUID contactId,
                                           @PathVariable("localId") UUID localId) throws DocumentException {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> contactService.deleteContact(user, ern, contactId, localId))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = POST, value = "/voted")
    public ResponseEntity<?> recordVote(@RequestBody @Valid RecordVoteRequest ern,
                                        Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> recordVotedService.recordVote(user, ern))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/ward/{wardCode}/street/labels", method = POST)
    public ResponseEntity<?> getLabelsPdfOfElectorsByTownStreet(
            @RequestBody @Valid ElectorsByStreetsRequest electorsByStreetsRequest,
            @PathVariable("wardCode") String wardCode,
            Principal principal) throws DocumentException {
        return requestValidator.validate(electorsByStreetsRequest)
                .flatMap(streets -> sessionService.extractUserFromPrincipal(principal))
                .flatMap(user -> labelService.generateLabelsPdf(electorsByStreetsRequest, wardCode, user))
                .fold(errorHandler::mapToResponseEntity, this::handlePdfResponse);
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
                .fold(errorHandler::mapToResponseEntity, this::handlePdfResponse);
    }

    private ResponseEntity<byte[]> handlePdfResponse(ByteArrayOutputStream outputStream) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
        return new ResponseEntity<>(outputStream.toByteArray(), responseHeaders, HttpStatus.OK);
    }
}
