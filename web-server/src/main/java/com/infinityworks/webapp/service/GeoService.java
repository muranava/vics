package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.gmaps.MapsClient;
import com.infinityworks.webapp.clients.gmaps.MapsRequestExecutor;
import com.infinityworks.webapp.config.AppProperties;
import com.infinityworks.webapp.error.MapsApiFailure;
import com.infinityworks.webapp.repository.StatsRepository;
import com.infinityworks.webapp.rest.dto.AddressLookupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class GeoService {
    private final Logger log = LoggerFactory.getLogger(GeoService.class);
    private static final int MAX_CONSTITUENCIES = 1000;

    private final MapsClient mapsClient;
    private final MapsRequestExecutor mapsRequestExecutor;
    private final StatsRepository statsRepository;
    private final TopoJsonEnricher topoJsonEnricher;
    private final String mapsApiKey;

    @Autowired
    public GeoService(MapsClient mapsClient,
                      MapsRequestExecutor mapsRequestExecutor,
                      StatsRepository statsRepository,
                      TopoJsonEnricher topoJsonEnricher,
                      AppProperties appProperties) {
        this.mapsClient = mapsClient;
        this.mapsRequestExecutor = mapsRequestExecutor;
        this.statsRepository = statsRepository;
        this.topoJsonEnricher = topoJsonEnricher;
        this.mapsApiKey = appProperties.getAddressLookupToken();
    }

    /**
     * Requests the geocoding of streets from google. It works by passing an address string to google, e.g.
     * "Boswell Drive, Coventry, CV2 2DH" and google returns the coordinates for that address.
     * <p>
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
                .map(address -> supplyAsync(() -> {
                    Call<Object> call = mapsClient.reverseLookupAddress(address, mapsApiKey);
                    return mapsRequestExecutor.execute(call);
                }))
                .collect(toList());
    }

    private List<Try<Object>> waitForAllRequestsToComplete(List<CompletableFuture<Try<Object>>> futures) {
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    public Try<byte[]> constituencyStatsMap(String regionName) {
        Map<String, BigInteger> counts = statsRepository.countMostRecordContactByConstituency(MAX_CONSTITUENCIES)
                .stream()
                .collect(toMap(row -> (String) row[2],
                        row -> (BigInteger) row[1]));
        try {
            return Try.success(topoJsonEnricher.addCanvassedCountsToUkMapByConstituency(regionName, counts).toByteArray());
        } catch (IOException e) {
            String msg = "Failed to construct topsjson map from constituencies";
            log.error(msg, e);
            return Try.failure(new IllegalStateException(msg));
        }
    }
}
