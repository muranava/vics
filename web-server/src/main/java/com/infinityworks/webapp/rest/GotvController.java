package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.rest.dto.GenerateGotvCardRequest;
import com.infinityworks.webapp.service.GotvService;
import com.infinityworks.webapp.service.SessionService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/gotv")
public class GotvController {

    private final RequestValidator requestValidator;
    private final SessionService sessionService;
    private final GotvService gotvService;
    private final RestErrorHandler restErrorHandler;

    @Autowired
    public GotvController(RequestValidator requestValidator,
                          SessionService sessionService,
                          GotvService gotvService,
                          RestErrorHandler restErrorHandler) {
        this.requestValidator = requestValidator;
        this.sessionService = sessionService;
        this.gotvService = gotvService;
        this.restErrorHandler = restErrorHandler;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/ward/{wardCode}/pdf", method = POST, produces = "application/pdf")
    public ResponseEntity<?> getPdfOfElectorsByTownStreet(
            @RequestBody @Valid GenerateGotvCardRequest request,
            @PathVariable("wardCode") String wardCode,
            Principal principal) throws DocumentException {
        return requestValidator.validate(request)
                .flatMap(req -> sessionService.extractUserFromPrincipal(principal))
                .flatMap(user -> gotvService.generateElectorsByStreet(wardCode, user, request))
                .fold(restErrorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
