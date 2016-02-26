package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.rest.dto.GenerateGotvCardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class GotvService {

    private final ElectorsService electorsService;

    @Autowired
    public GotvService(ElectorsService electorsService) {
        this.electorsService = electorsService;
    }

    public Try<ByteArrayOutputStream> generateElectorsByStreet(String wardCode,
                                                               User user,
                                                               GenerateGotvCardRequest request) {
        return electorsService.electorsByStreets(request.getTownStreets(), wardCode, user);
    }
}
