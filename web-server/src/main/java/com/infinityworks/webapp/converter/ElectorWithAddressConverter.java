package com.infinityworks.webapp.converter;

import com.google.common.base.Function;
import com.infinityworks.webapp.domain.ElectorWithAddress;
import com.infinityworks.webapp.rest.dto.ElectorResponse;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ElectorWithAddressConverter implements Function<List<ElectorWithAddress>, List<ElectorResponse>> {
    @Override
    public List<ElectorResponse> apply(List<ElectorWithAddress> electors) {
        return electors.stream().map(elector -> new ElectorResponse(
                elector.getWardCode(),
                elector.getPollingDistrict(),
                elector.getElectorId(),
                elector.getElectorSuffix(),
                elector.getTitle(),
                elector.getFirstName(),
                elector.getLastName(),
                elector.getInitial(),
                elector.getHouse(),
                elector.getStreet()))
                .collect(toList());
    }
}
