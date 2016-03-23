package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.gmaps.command.AddressLookupCommand;
import com.infinityworks.webapp.clients.gmaps.command.AddressLookupCommandFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeoService {
    private Map<String, Object> cache = new ConcurrentHashMap<>();
    private final AddressLookupCommandFactory addressLookupCommandFactory;

    @Autowired
    public GeoService(AddressLookupCommandFactory addressLookupCommandFactory) {
        this.addressLookupCommandFactory = addressLookupCommandFactory;
    }

    public Try<Object> reverseGeolocateAddress(String searchTerm) {
        if (cache.containsKey(searchTerm)) {
            return Try.success(cache.get(searchTerm));
        }

        AddressLookupCommand lookupCommand = addressLookupCommandFactory.create(searchTerm);
        Try<Object> result = lookupCommand.execute();
        result.map(address -> cache.put(searchTerm, address));

        return result;
    }
}
