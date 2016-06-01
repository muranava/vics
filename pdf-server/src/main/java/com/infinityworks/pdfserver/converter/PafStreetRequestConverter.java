package com.infinityworks.pdfserver.converter;

import com.infinityworks.pafclient.dto.ImmutablePafStreetRequest;
import com.infinityworks.pafclient.dto.PafStreetRequest;
import com.infinityworks.pdfserver.controller.dto.StreetRequest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PafStreetRequestConverter implements Function<StreetRequest, PafStreetRequest> {
    @Override
    public PafStreetRequest apply(StreetRequest street) {
        return ImmutablePafStreetRequest.builder()
                .withMainStreet(street.getMainStreet())
                .withDependentStreet(street.getDependentStreet())
                .withDependentLocality(street.getDependentLocality())
                .withPostTown(street.getPostTown())
                .build();
    }
}
