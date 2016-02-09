package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.error.ErrorHandler;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Resource to provide information about wards and constituencies
 */
@RestController
@RequestMapping("/ward")
public class WardController {
    private final WardService wardService;
    private final ErrorHandler errorHandler;

    @Autowired
    public WardController(WardService wardService, ErrorHandler errorHandler) {
        this.wardService = wardService;
        this.errorHandler = errorHandler;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET, value = "/constituency/{id}")
    public ResponseEntity<?> wardsByConstituencyId(@PathVariable(value = "id") String id) {
        Try<UUID> constituencyId;
        try {
            constituencyId = Try.success(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            constituencyId = Try.failure(new NotFoundFailure("No constituency with ID"));
        }

        return constituencyId
                .flatMap(wardService::findByConstituency)
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
