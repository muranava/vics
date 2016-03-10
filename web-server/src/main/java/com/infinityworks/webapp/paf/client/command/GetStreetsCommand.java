package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.rest.dto.Street;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.List;

public class GetStreetsCommand extends HystrixCommand<Try<List<Street>>> {
    private final String wardCode;
    private final PafClient pafClient;

    public GetStreetsCommand(String wardCode, PafClient pafClient, int timeoutMSecs) {
        super(HystrixCommandGroupKey.Factory.asKey("GetStreets"), timeoutMSecs);
        this.wardCode = wardCode;
        this.pafClient = pafClient;
    }

    @Override
    protected Try<List<Street>> run() throws Exception {
        return pafClient.findStreetsByWardCode(wardCode);
    }
}
