package com.infinityworks.webapp.paf.dto;

import com.infinityworks.webapp.paf.dto.PafStreet;
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
