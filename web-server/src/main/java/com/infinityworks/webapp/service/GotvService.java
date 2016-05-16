package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class GotvService {

    private final VoterService voterService;
    private final TableBuilder tableBuilder;
    private final DocumentBuilder documentBuilder;

    @Autowired
    public GotvService(VoterService voterService,
                       @Qualifier("gotv") TableBuilder tableBuilder,
                       @Qualifier("gotv") DocumentBuilder documentBuilder) {
        this.voterService = voterService;
        this.tableBuilder = tableBuilder;
        this.documentBuilder = documentBuilder;
    }

    public Try<ByteArrayOutputStream> generateElectorsByStreet(String wardCode,
                                                               User user,
                                                               ElectorsByStreetsRequest request) {
        return voterService.getPdfOfFilteredElectorsByStreet(tableBuilder, documentBuilder, request, wardCode, user);
    }
}
