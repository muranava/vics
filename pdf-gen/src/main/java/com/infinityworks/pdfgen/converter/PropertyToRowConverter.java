package com.infinityworks.pdfgen.converter;

import com.infinityworks.commondto.Property;
import com.infinityworks.pdfgen.model.ElectorRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static com.infinityworks.pdfgen.model.ElectorRowBuilder.electorRow;
import static java.util.stream.Collectors.toList;

@Component
public class PropertyToRowConverter implements BiFunction<String, Property, List<ElectorRow>> {
    private static final Logger log = LoggerFactory.getLogger(PropertyToRowConverter.class);

    @Override
    public List<ElectorRow> apply(String wardCode, Property property) {
        log.debug("Property: {}", property);

        return property.getVoters().stream()
                .map(voter -> electorRow()
                        .withHouse(property.getHouseNumber())
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
                        .withStreet(property.getStreetLabel() + ", " + property.getPostCode())
                        .withErn(voter.getErn())
                        .build())
                .collect(toList());
    }
}
