package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommand;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommandFactory;
import com.infinityworks.webapp.converter.PafToStreetConverter;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

@Service
public class PafAddressService {
    private final GetStreetsCommandFactory getStreetsCommandFactory;
    private final PafToStreetConverter pafToStreetConverter;
    private Map<String, List<Street>> cache = new ConcurrentHashMap<>();

    @Autowired
    public PafAddressService(GetStreetsCommandFactory getStreetsCommandFactory, PafToStreetConverter pafToStreetConverter) {
        this.getStreetsCommandFactory = getStreetsCommandFactory;
        this.pafToStreetConverter = pafToStreetConverter;
    }

    public Try<List<Street>> getStreetsByWard(String wardCode) {
        if (cache.containsKey(wardCode)) {
            return Try.success(cache.get(wardCode));
        }

        GetStreetsCommand getStreetsCommand = getStreetsCommandFactory.create(wardCode);
        Try<List<Street>> streets = getStreetsCommand.execute().map(str -> str.response().stream()
                .map(pafToStreetConverter)
                .collect(toList()));
        streets.map(s -> cache.putIfAbsent(wardCode, s));
        return streets;
    }
}
