package com.infinityworks.pdfserver.controller;

import com.infinityworks.pdfserver.controller.dto.GeneratePdfRequest;
import com.infinityworks.pdfserver.error.ErrorHandler;
import com.infinityworks.pdfserver.service.AddressLabelGenerator;
import com.infinityworks.pdfserver.service.CanvassCardGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/canvass")
public class PdfController {
    private final CanvassCardGenerator canvassCardGenerator;
    private final AddressLabelGenerator addressLabelGenerator;
    private final ErrorHandler errorHandler;

    @Autowired
    public PdfController(CanvassCardGenerator canvassCardGenerator,
                         AddressLabelGenerator addressLabelGenerator,
                         ErrorHandler errorHandler) {
        this.canvassCardGenerator = canvassCardGenerator;
        this.addressLabelGenerator = addressLabelGenerator;
        this.errorHandler = errorHandler;
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> generateCanvassCard(@RequestBody GeneratePdfRequest request) {
        return canvassCardGenerator
                .generateCanvassCard(request)
                .fold(errorHandler::mapToResponse, this::handlePdfResponse);
    }

    @RequestMapping(method = POST, value = "/gotv")
    public ResponseEntity<?> generateGotvCanvassCard(@RequestBody GeneratePdfRequest request) {
        return canvassCardGenerator
                .generateCanvassCard(request)
                .fold(errorHandler::mapToResponse, this::handlePdfResponse);
    }

    @RequestMapping(method = POST, value = "/gotpv")
    public ResponseEntity<?> generateGotpvCanvassCard(@RequestBody GeneratePdfRequest request) {
        return addressLabelGenerator
                .generateAddressLabelsForPostalVoters(request)
                .fold(errorHandler::mapToResponse, this::handlePdfResponse);
    }

    private ResponseEntity<byte[]> handlePdfResponse(ByteArrayOutputStream outputStream) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(outputStream.toByteArray(), responseHeaders, HttpStatus.OK);
    }
}
