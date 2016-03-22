package com.infinityworks.webapp.clients.gmaps.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.gmaps.MapsClient;
import com.infinityworks.webapp.clients.gmaps.MapsRequestExecutor;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import retrofit2.Call;

public class AddressLookupCommand extends HystrixCommand<Try<Object>> {

    private final String searchTerm;
    private final MapsClient mapsClient;
    private final String apiKey;
    private final MapsRequestExecutor responseHandler;

    public AddressLookupCommand(String searchTerm,
                                MapsClient mapsClient,
                                int timeoutMSecs,
                                String apiKey,
                                MapsRequestExecutor responseHandler) {
        super(HystrixCommandGroupKey.Factory.asKey("AddressLookup"), timeoutMSecs);
        this.searchTerm = searchTerm;
        this.mapsClient = mapsClient;
        this.apiKey = apiKey;
        this.responseHandler = responseHandler;
    }

    @Override
    protected Try<Object> run() throws Exception {
        Call<Object> response = mapsClient.reverseLookupAddress(searchTerm, apiKey);
        return responseHandler.execute(response);
    }
}
