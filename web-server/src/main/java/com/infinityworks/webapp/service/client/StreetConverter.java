package com.infinityworks.webapp.service.client;

import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StreetConverter implements Function<Street, StreetRequest> {
    @Override
    public StreetRequest apply(Street street) {
        return new StreetRequest(street.getMainStreet(), street.getPostTown(),
                street.getDependentStreet(), street.getDependentLocality());
    }
}
