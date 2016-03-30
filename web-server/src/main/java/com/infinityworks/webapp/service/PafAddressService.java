package com.infinityworks.webapp.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommand;
import com.infinityworks.webapp.clients.paf.command.GetStreetsCommandFactory;
import com.infinityworks.webapp.converter.PafToStreetResponseConverter;
import com.infinityworks.webapp.rest.dto.StreetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PafAddressService {
    private final GetStreetsCommandFactory getStreetsCommandFactory;
    private final PafToStreetResponseConverter pafToStreetConverter;
    private final Cache<String, List<StreetResponse>> cache = CacheBuilder.newBuilder().build();

    @Autowired
    public PafAddressService(GetStreetsCommandFactory getStreetsCommandFactory, PafToStreetResponseConverter pafToStreetConverter) {
        this.getStreetsCommandFactory = getStreetsCommandFactory;
        this.pafToStreetConverter = pafToStreetConverter;
    }

    public Try<List<StreetResponse>> getStreetsByWard(String wardCode) {
        List<StreetResponse> cacheHit = cache.getIfPresent(wardCode);
        if (cacheHit != null) {
            return Try.success(cacheHit);
        }

        GetStreetsCommand getStreetsCommand = getStreetsCommandFactory.create(wardCode);
        Try<List<StreetResponse>> streets = getStreetsCommand.execute().map(str -> str.response().stream()
                .map(pafToStreetConverter)
                .collect(toList()));
        streets.accept(s -> cache.put(wardCode, s));
        return streets;
    }
}
