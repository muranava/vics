package com.infinityworks.pdfserver.converter;

import com.infinityworks.pafclient.dto.*;
import com.infinityworks.pdfserver.controller.dto.GeneratePdfRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class GotpvRequestAdapter implements Function<GeneratePdfRequest, GotvVoterRequest> {
    private final PafStreetRequestConverter streetConverter;

    @Autowired
    public GotpvRequestAdapter(PafStreetRequestConverter streetConverter) {
        this.streetConverter = streetConverter;
    }

    @Override
    public GotvVoterRequest apply(GeneratePdfRequest request) {
        GotvFilterFlags flags = ImmutableGotvFilterFlags.builder()
                .withHasPV(Boolean.TRUE)
                .withDeceased(Boolean.FALSE)
                .withVoted(Boolean.FALSE)
                .build();

        List<PafStreetRequest> streets = request.getStreets().stream()
                .map(streetConverter)
                .collect(toList());

        GotvFilter filter = ImmutableGotvFilter.builder().withFlags(flags).build();

        return ImmutableGotvVoterRequest.builder()
                .withStreets(streets)
                .withFilter(filter)
                .build();
    }
}
