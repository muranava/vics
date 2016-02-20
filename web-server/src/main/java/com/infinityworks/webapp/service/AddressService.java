package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.service.client.PafClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final Logger log = LoggerFactory.getLogger(AddressService.class);
    private final WardService wardService;
    private final PafClient pafClient;

    @Autowired
    public AddressService(WardService wardService, PafClient pafClient) {
        this.wardService = wardService;
        this.pafClient = pafClient;
    }

    public Try<List<Street>> getTownStreetsByWardCode(String wardCode, User user) {
        Optional<Ward> ward = wardService.findByCode(wardCode).stream().findFirst();
        if (!ward.isPresent()) {
            return Try.failure(new NotFoundFailure("No ward with code=" + wardCode));
        }
        if (!user.hasWardPermission(ward.get())) {
            log.error("User={} tried to access towns by wardCode={}", user, wardCode);
            return Try.failure(new NotAuthorizedFailure("Content forbidden"));
        }
        return pafClient.findStreetsByWardCode(wardCode);
    }
}
