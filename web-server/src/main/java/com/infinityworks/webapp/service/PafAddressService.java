package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommand;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommandFactory;
import com.infinityworks.webapp.converter.PafToStreetResponseConverter;
import com.infinityworks.webapp.rest.dto.ImmutableStreetsByWardResponse;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.rest.dto.StreetsByWardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PafAddressService {
    private final GetStreetsCommandFactory getStreetsCommandFactory;
    private final PafToStreetResponseConverter pafToStreetConverter;

    @Autowired
    public PafAddressService(GetStreetsCommandFactory getStreetsCommandFactory, PafToStreetResponseConverter pafToStreetConverter) {
        this.getStreetsCommandFactory = getStreetsCommandFactory;
        this.pafToStreetConverter = pafToStreetConverter;
    }

    public Try<StreetsByWardResponse> getStreetsByWard(String wardCode) {
        GetStreetsCommand getStreetsCommand = getStreetsCommandFactory.create(wardCode);
        return getStreetsCommand.execute().map(str -> {
            List<Street> streets = str.response()
                    .stream()
                    .map(pafToStreetConverter)
                    .collect(toList());

            return ImmutableStreetsByWardResponse.builder()
                    .withStats(str.stats())
                    .withStreets(streets)
                    .build();
        });
    }
}
