package com.infinityworks.webapp.clients.paf.converter;

import com.infinityworks.webapp.clients.paf.dto.ImmutablePafStreetRequest;
import com.infinityworks.webapp.clients.paf.dto.PafStreetRequest;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PafStreetRequestConverter implements Function<com.infinityworks.webapp.rest.dto.Street, PafStreetRequest> {
    @Override
    public PafStreetRequest apply(Street street) {
        return ImmutablePafStreetRequest.of(
                street.getMainStreet(),
                street.getPostTown(),
                street.getDependentStreet(),
                street.getDependentLocality());
    }
}
