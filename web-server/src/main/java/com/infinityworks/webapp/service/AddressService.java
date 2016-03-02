package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final WardService wardService;
    private final PafClient pafClient;

    @Autowired
    public AddressService(WardService wardService, PafClient pafClient) {
        this.wardService = wardService;
        this.pafClient = pafClient;
    }

    public Try<List<Street>> getTownStreetsByWardCode(String wardCode, Permissible permissible) {
        return wardService
                .getByCode(wardCode, permissible)
                .flatMap(ward -> pafClient.findStreetsByWardCode(wardCode));
    }
}
