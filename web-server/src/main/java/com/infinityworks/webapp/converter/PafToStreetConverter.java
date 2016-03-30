package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.dto.PafStreetResponse;
import com.infinityworks.webapp.rest.dto.Street;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PafToStreetConverter implements Function<PafStreetResponse, Street> {
    @Override
    public Street apply(PafStreetResponse pafStreet) {
        return new Street(
                pafStreet.mainStreet(),
                pafStreet.postTown(),
                pafStreet.dependentStreet(),
                pafStreet.dependentLocality());
    }
}
