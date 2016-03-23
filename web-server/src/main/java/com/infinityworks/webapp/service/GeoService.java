package com.infinityworks.webapp.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.gmaps.command.AddressLookupCommand;
import com.infinityworks.webapp.clients.gmaps.command.AddressLookupCommandFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoService {
    private final Cache<String, Object> cache = CacheBuilder.newBuilder().build();
    private final AddressLookupCommandFactory addressLookupCommandFactory;

    @Autowired
    public GeoService(AddressLookupCommandFactory addressLookupCommandFactory) {
        this.addressLookupCommandFactory = addressLookupCommandFactory;
    }

    public Try<Object> reverseGeolocateAddress(String searchTerm) {
        Object value = cache.getIfPresent(searchTerm);
        if (value != null) {
            return Try.success(value);
        }

        AddressLookupCommand lookupCommand = addressLookupCommandFactory.create(searchTerm);
        Try<Object> result = lookupCommand.execute();
        result.accept(address -> cache.put(searchTerm, address));

        return result;
    }
}
