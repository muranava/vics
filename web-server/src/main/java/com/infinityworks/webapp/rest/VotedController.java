package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/voted")
public class VotedController {

    private final SessionService sessionService;
    private final VoteService voteService;
    private final RestErrorHandler errorHandler;

    @Autowired
    public VotedController(SessionService sessionService,
                           VoteService voteService,
                           RestErrorHandler errorHandler) {
        this.sessionService = sessionService;
        this.voteService = voteService;
        this.errorHandler = errorHandler;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = POST, value = "/{ern}")
    public ResponseEntity<?> recordVote(@PathVariable(value = "ern") String ern,
                                        Principal principal) {
        return sessionService.extractUserFromPrincipal(principal)
                .flatMap(user -> voteService.recordVote(ern))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
