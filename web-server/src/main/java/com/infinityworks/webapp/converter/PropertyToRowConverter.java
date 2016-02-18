package com.infinityworks.webapp.converter;

import com.infinityworks.pdfgen.ElectorRow;
import com.infinityworks.webapp.service.client.Property;
import com.infinityworks.webapp.service.client.Voter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.infinityworks.pdfgen.ElectorRowBuilder.electorRow;
import static com.infinityworks.webapp.common.lang.StringExtras.isNullOrEmpty;
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
                        .withErn(generateErn(wardCode, voter))
                        .build()).collect(toList());
    }

    private String generateErn(String wardCode, Voter voter) {
            String mandatoryPart = wardCode + "/" + voter.getElectorId();
            return isNullOrEmpty(voter.getElectorSuffix())
                    ? mandatoryPart
                    : mandatoryPart + "/" + voter.getElectorSuffix();
    }
}
