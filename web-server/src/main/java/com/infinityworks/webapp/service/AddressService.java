package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.paf.client.command.GetStreetsCommand;
import com.infinityworks.webapp.paf.client.command.GetStreetsCommandFactory;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final WardService wardService;
    private final GetStreetsCommandFactory getStreetsCommandFactory;

    @Autowired
    public AddressService(WardService wardService, GetStreetsCommandFactory getStreetsCommandFactory) {
        this.wardService = wardService;
        this.getStreetsCommandFactory = getStreetsCommandFactory;
    }

    public Try<List<Street>> getTownStreetsByWardCode(String wardCode, User user) {
        return wardService
                .getByCode(wardCode, user)
                .flatMap(ward -> getStreetsCommandFactory.create(wardCode).execute());
    }
}
