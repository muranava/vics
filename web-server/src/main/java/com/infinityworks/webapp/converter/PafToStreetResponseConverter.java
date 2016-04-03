package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.dto.PafStreetResponse;
import com.infinityworks.webapp.rest.dto.StreetResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PafToStreetResponseConverter implements Function<PafStreetResponse, StreetResponse> {
    @Override
    public StreetResponse apply(PafStreetResponse pafStreet) {
        return new StreetResponse(
                pafStreet.mainStreet(),
                pafStreet.postTown(),
                pafStreet.dependentStreet(),
                pafStreet.dependentLocality(),
                pafStreet.voters(),
                pafStreet.canvassed(),
                pafStreet.priority());
    }
}
