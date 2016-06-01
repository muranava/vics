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
public class GotvService {
    private static final Logger log = LoggerFactory.getLogger(GotvService.class);
    private final PdfClient pdfServerClient;
    private final WardService wardService;

    @Autowired
    public GotvService(PdfClient pdfServerClient, WardService wardService) {
        this.pdfServerClient = pdfServerClient;
        this.wardService = wardService;
    }

    public Try<byte[]> generateGotvCanvassCard(User user, String wardCode, ElectorsByStreetsRequest request) {
        return wardService.getByCode(wardCode, user)
                .flatMap(ward -> {
                    RequestInfo requestInfo = new RequestInfo(wardCode, ward.getName(), ward.getConstituency().getName());
                    GeneratePdfRequest generatePdfRequest = new GeneratePdfRequest(request.getStreets(), request.getFlags(), requestInfo);
                    return pdfServerClient.requestGotvCanvassCard(generatePdfRequest);
                }).map(content -> {
                    log.info("User={} generated address labels for GOTPV. ward={}", user, wardCode);
                    return content;
                });
    }
}
