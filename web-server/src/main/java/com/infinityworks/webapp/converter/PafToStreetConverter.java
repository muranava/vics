package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.paf.dto.PafStreet;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PafToStreetConverter implements Function<PafStreet, Street> {
    @Override
    public Street apply(PafStreet pafStreet) {
        return new Street(
                pafStreet.getMainStreet(),
                pafStreet.getPostTown(),
                pafStreet.getDependentStreet(),
                pafStreet.getDependentLocality());
    }
}
