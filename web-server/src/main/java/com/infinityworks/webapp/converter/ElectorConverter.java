package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.domain.Address;
import com.infinityworks.webapp.domain.Elector;
import com.infinityworks.webapp.rest.dto.ElectorResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

@Component
public class ElectorConverter implements BiFunction<List<Elector>, Address, List<ElectorResponse>> {
    @Override
    public List<ElectorResponse> apply(List<Elector> electors, Address address) {
        return electors.stream().map(elector -> new ElectorResponse(
                elector.getWardCode(),
                elector.getPollingDistrict(),
                elector.getElectorId(),
                elector.getElectorSuffix(),
                elector.getTitle(),
                elector.getFirstName(),
                elector.getLastName(),
                elector.getInitial(),
                address.getHouse(),
                address.getStreet()))
                .collect(toList());
    }
}
