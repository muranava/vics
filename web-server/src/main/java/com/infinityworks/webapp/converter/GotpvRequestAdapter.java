package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.converter.PafStreetRequestConverter;
import com.infinityworks.webapp.clients.paf.dto.*;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class GotpvRequestAdapter implements Function<ElectorsByStreetsRequest, GotvVoterRequest> {
    private final PafStreetRequestConverter streetConverter;

    @Autowired
    public GotpvRequestAdapter(PafStreetRequestConverter streetConverter) {
        this.streetConverter = streetConverter;
    }

    @Override
    public GotvVoterRequest apply(ElectorsByStreetsRequest electorsByStreetsRequest) {
        GotvFilterFlags flags = ImmutableGotvFilterFlags.builder()
                .withHasPV(Boolean.TRUE)
                .withDeceased(Boolean.FALSE)
                .build();

        List<PafStreetRequest> streets = electorsByStreetsRequest.getStreets().stream()
                .map(streetConverter)
                .collect(toList());

        ImmutableGotvFilter filter = ImmutableGotvFilter.builder().withFlags(flags).build();

        return ImmutableGotvVoterRequest.builder()
                .withStreets(streets)
                .withFilter(filter)
                .build();
    }
}
