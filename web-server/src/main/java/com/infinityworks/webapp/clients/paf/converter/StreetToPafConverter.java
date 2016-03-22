package com.infinityworks.webapp.clients.paf.converter;

import com.infinityworks.webapp.clients.paf.dto.ImmutablePafStreet;
import com.infinityworks.webapp.clients.paf.dto.PafStreet;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StreetToPafConverter implements Function<com.infinityworks.webapp.rest.dto.Street, PafStreet> {
    @Override
    public PafStreet apply(Street street) {
        return ImmutablePafStreet.of(
                street.getMainStreet(),
                street.getPostTown(),
                street.getDependentStreet(),
                street.getDependentLocality());
    }
}
