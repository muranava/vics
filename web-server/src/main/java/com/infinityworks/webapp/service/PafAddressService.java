package com.infinityworks.webapp.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommand;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommandFactory;
import com.infinityworks.webapp.converter.PafToStreetConverter;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PafAddressService {
    private final GetStreetsCommandFactory getStreetsCommandFactory;
    private final PafToStreetConverter pafToStreetConverter;
    private final Cache<String, List<Street>> cache = CacheBuilder.newBuilder().build();

    @Autowired
    public PafAddressService(GetStreetsCommandFactory getStreetsCommandFactory, PafToStreetConverter pafToStreetConverter) {
        this.getStreetsCommandFactory = getStreetsCommandFactory;
        this.pafToStreetConverter = pafToStreetConverter;
    }

    public Try<List<Street>> getStreetsByWard(String wardCode) {
        List<Street> cacheHit = cache.getIfPresent(wardCode);
        if (cacheHit != null) {
            return Try.success(cacheHit);
        }

        GetStreetsCommand getStreetsCommand = getStreetsCommandFactory.create(wardCode);
        Try<List<Street>> streets = getStreetsCommand.execute().map(str -> str.response().stream()
                .map(pafToStreetConverter)
                .collect(toList()));
        streets.accept(s -> cache.put(wardCode, s));
        return streets;
    }
}
