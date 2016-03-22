package com.infinityworks.webapp.clients.gmaps.command;

import com.infinityworks.webapp.clients.gmaps.MapsClient;
import com.infinityworks.webapp.clients.gmaps.MapsRequestExecutor;

public class AddressLookupCommandFactory {
    private final int timeoutMsecs;
    private final MapsClient mapsClient;
    private final String apiKey;
    private final MapsRequestExecutor requestExecutor;

    public AddressLookupCommandFactory(MapsClient mapsClient, int timeoutMsecs, String apiKey, MapsRequestExecutor requestExecutor) {
        this.timeoutMsecs = timeoutMsecs;
        this.mapsClient = mapsClient;
        this.apiKey = apiKey;
        this.requestExecutor = requestExecutor;
    }

    public AddressLookupCommand create(String searchTerm) {
        return new AddressLookupCommand(searchTerm, mapsClient, timeoutMsecs, apiKey, requestExecutor);
    }
}
