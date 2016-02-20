package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.ElectorsService;
import com.infinityworks.webapp.service.SessionService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("elector")
public class ElectorsController {

    private final ElectorsService electorsService;
    private final RequestValidator requestValidator;
    private final SessionService sessionService;

    @Autowired
    public ElectorsController(ElectorsService electorsService,
                              RequestValidator requestValidator,
                              SessionService sessionService) {
        this.electorsService = electorsService;
        this.requestValidator = requestValidator;
        this.sessionService = sessionService;
    }

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
