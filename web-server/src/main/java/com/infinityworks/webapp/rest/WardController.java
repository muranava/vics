package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.error.ErrorHandler;
import com.infinityworks.webapp.rest.dto.ConstituencyName;
import com.infinityworks.webapp.rest.dto.ElectorsByWardAndConstituencyRequest;
import com.infinityworks.webapp.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Resource to provide information about wards and constituencies
 */
@RestController
@RequestMapping("/ward")
public class WardController {
    private final WardService wardService;
    private final ErrorHandler errorHandler;
    private final RequestValidator requestValidator;

    @Autowired
    public WardController(WardService wardService, ErrorHandler errorHandler, RequestValidator requestValidator) {
        this.wardService = wardService;
        this.errorHandler = errorHandler;
        this.requestValidator = requestValidator;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<?> wardsByConstituencyName(@RequestParam(required = true) String constituency) {
        return requestValidator
                .validate(ConstituencyName.of(constituency))
                .flatMap(constituencyName -> wardService.findByConstituencyName(constituencyName.getName()))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> wardsByWardNamesAndConstituencyName(@RequestBody ElectorsByWardAndConstituencyRequest electorRequest) {
        return requestValidator
                .validate(electorRequest)
                .flatMap(request -> wardService.findByConstituencyNameAndWardNames(request.getConstituencyName(), request.getWardNames()))
                .fold(errorHandler::mapToResponseEntity, ResponseEntity::ok);
    }
}
