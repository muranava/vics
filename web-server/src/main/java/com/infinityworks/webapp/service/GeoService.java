package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.gmaps.command.AddressLookupCommandFactory;
import com.infinityworks.webapp.error.MapsApiFailure;
import com.infinityworks.webapp.rest.dto.AddressLookupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

@Service
public class GeoService {
    private final AddressLookupCommandFactory addressLookupCommandFactory;
    private final Logger log = LoggerFactory.getLogger(GeoService.class);

    @Autowired
    public GeoService(AddressLookupCommandFactory addressLookupCommandFactory) {
        this.addressLookupCommandFactory = addressLookupCommandFactory;
    }

    /**
     * Requests the geocoding of streets from google. It works by passing an address string to google, e.g.
     * "Boswell Drive, Coventry, CV2 2DH" and google returns the coordinates for that address.
     *
     * The addresses requests are executed in parallel since google can only geocode a single address per request
     *
     * @param addresses the addresses to geocode
     * @return the coordinates for the given addresses
     */
    public Try<List<Object>> reverseGeolocateAddress(AddressLookupRequest addresses) {
        List<CompletableFuture<Try<Object>>> futures = executeGeocodeRequestsAsync(addresses);
        List<Try<Object>> results = waitForAllRequestsToComplete(futures);
        if (results.stream().anyMatch(Try::isFailure)) {
            log.warn("Failed to geocode addresses");
            return Try.failure(new MapsApiFailure("Could not geocode all streets"));
        } else {
            return Try.success(results.stream().map(Try::get).collect(toList()));
        }
    }

    private List<CompletableFuture<Try<Object>>> executeGeocodeRequestsAsync(AddressLookupRequest request) {
        return request.addresses()
                .stream()
                .map(address -> supplyAsync(() -> addressLookupCommandFactory.create(address).execute()))
                .collect(toList());
    }

    private List<Try<Object>> waitForAllRequestsToComplete(List<CompletableFuture<Try<Object>>> futures) {
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }
}
