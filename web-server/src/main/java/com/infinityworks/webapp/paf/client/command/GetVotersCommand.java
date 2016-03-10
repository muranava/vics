package com.infinityworks.webapp.paf.client.command;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.dto.PropertyResponse;
import com.infinityworks.webapp.rest.dto.Street;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.List;

public class GetVotersCommand extends HystrixCommand<Try<PropertyResponse>> {
    private final List<Street> streets;
    private final String wardCode;
    private final PafClient pafClient;

    public GetVotersCommand(List<Street> streets, String wardCode, PafClient pafClient, int timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("GetVoters"), timeout);
        this.streets = streets;
        this.wardCode = wardCode;
        this.pafClient = pafClient;
    }

    @Override
    protected Try<PropertyResponse> run() throws Exception {
        return pafClient.findVotersByStreet(streets, wardCode);
    }
}
