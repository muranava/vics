package com.infinityworks.pdfgen.converter;

import com.infinityworks.commondto.Property;
import com.infinityworks.pdfgen.model.ElectorRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static com.infinityworks.pdfgen.model.ElectorRowBuilder.electorRow;
import static java.util.stream.Collectors.toList;

@Component
public class PropertyToRowConverter implements BiFunction<String, Property, List<ElectorRow>> {

    /**
     * FIXME some of these values are not yet available in the API
     */
    @Override
    public List<ElectorRow> apply(String wardCode, Property property) {
        return property.getVoters().stream()
                .map(voter -> electorRow()
                        .withHouse(property.getBuildingNumber())
                        .withName(voter.getLastName() + ", " + voter.getFirstName())
                        .withTelephone(voter.getTelephone())
                        .withLikelihood("")
                        .withIssue1("")
                        .withIssue2("")
                        .withIssue3("")
                        .withSupport("")
                        .withHasPV("")
                        .withWantsPV("")
                        .withNeedsLift("")
                        .withPoster("")
                        .withDeceased("")
                        .withStreet(property.getStreetLabel())
                        .withErn(voter.getErn())
                        .build()).collect(toList());
    }
}
