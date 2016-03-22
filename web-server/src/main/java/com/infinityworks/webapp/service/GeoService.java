package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.gmaps.command.AddressLookupCommand;
import com.infinityworks.webapp.clients.gmaps.command.AddressLookupCommandFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoService {

    private final AddressLookupCommandFactory addressLookupCommandFactory;

    @Autowired
    public GeoService(AddressLookupCommandFactory addressLookupCommandFactory) {
        this.addressLookupCommandFactory = addressLookupCommandFactory;
    }

    public Try<Object> reverseGeolocateAddress(String searchTerm) {
        AddressLookupCommand lookupCommand = addressLookupCommandFactory.create(searchTerm);
        return lookupCommand.execute();
    }
}
