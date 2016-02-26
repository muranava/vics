package com.infinityworks.webapp.service.client;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StreetToPafConverter implements Function<com.infinityworks.webapp.rest.dto.Street, PafStreet> {
    @Override
    public PafStreet apply(com.infinityworks.webapp.rest.dto.Street street) {
        return new PafStreet(street.getMainStreet(), street.getPostTown(),
                street.getDependentStreet(), street.getDependentLocality());
    }
}
