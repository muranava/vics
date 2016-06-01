package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.pdfserver.PdfClient;
import com.infinityworks.webapp.clients.pdfserver.dto.GeneratePdfRequest;
import com.infinityworks.webapp.clients.pdfserver.dto.RequestInfo;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CanvassCardService {
    private final Logger log = LoggerFactory.getLogger(CanvassCardService.class);

    private final WardService wardService;
    private final PdfClient pdfServerClient;

    @Autowired
    public CanvassCardService(WardService wardService, PdfClient pdfServerClient) {
        this.wardService = wardService;
        this.pdfServerClient = pdfServerClient;
    }

    public Try<byte[]> generateCanvassCard(ElectorsByStreetsRequest request, String wardCode, User user) {
        return wardService.getByCode(wardCode, user)
                .flatMap(ward -> {
                    RequestInfo requestInfo = new RequestInfo(wardCode, ward.getName(), ward.getConstituency().getName());
                    GeneratePdfRequest generatePdfRequest = new GeneratePdfRequest(request.getStreets(), null, requestInfo);

                    return pdfServerClient.requestCanvassCard(generatePdfRequest);
                }).map(content -> {
                    log.info("User={} generated canvass card for ward={}, numStreets={}", user, wardCode, request.getStreets().size());
                    return content;
                });
    }
}
