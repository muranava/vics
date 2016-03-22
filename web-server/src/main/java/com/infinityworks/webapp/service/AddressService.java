package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.converter.PafToStreetConverter;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommand;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommandFactory;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AddressService {
    private final WardService wardService;
    private final GetStreetsCommandFactory getStreetsCommandFactory;
    private final PafToStreetConverter pafToStreetConverter;

    @Autowired
    public AddressService(WardService wardService,
                          GetStreetsCommandFactory getStreetsCommandFactory,
                          PafToStreetConverter pafToStreetConverter) {
        this.wardService = wardService;
        this.getStreetsCommandFactory = getStreetsCommandFactory;
        this.pafToStreetConverter = pafToStreetConverter;
    }

    public Try<List<Street>> getTownStreetsByWardCode(String wardCode, User user) {
        return wardService
                .getByCode(wardCode, user)
                .flatMap(ward -> {
                    GetStreetsCommand getStreetsCommand = getStreetsCommandFactory.create(ward.getCode());
                    return getStreetsCommand.execute();
                })
                .map(streets -> streets.response().stream()
                        .map(pafToStreetConverter)
                        .collect(toList()));
    }
}
