package com.infinityworks.webapp.converter;

import com.infinityworks.pdfgen.model.ElectorRow;
import com.infinityworks.pdfgen.model.ElectorRowBuilder;
import com.infinityworks.webapp.paf.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static com.infinityworks.common.lang.StringExtras.nullToEmpty;
import static com.infinityworks.pdfgen.model.ElectorRowBuilder.electorRow;
import static java.util.stream.Collectors.toList;

@Component
public class PropertyToRowConverter implements BiFunction<String, Property, List<ElectorRow>> {
    private static final Logger log = LoggerFactory.getLogger(PropertyToRowConverter.class);

    @Override
    public List<ElectorRow> apply(String wardCode, Property property) {
        log.debug("Property: {}", property);

        return property.voters().stream()
                .map(voter -> {
                    Issues issues = voter.getIssues();
                    Volunteer volunteer = voter.getVolunteer();
                    Voting voting = voter.getVoting();
                    Flags flags = voter.getFlags();

                    ElectorRowBuilder row = electorRow()
                            .withHouse(property.house())
                            .withName(voter.getLastName() + ", " + voter.getFirstName())
                            .withTelephone(voter.getTelephone());

                    if (voting != null) {
                        row.withLikelihood(nullToEmpty(voting.getLikelihood()))
                                .withSupport(nullToEmpty(voting.getIntention()));
                    }

                    if (issues != null) {
                        row.withIssue1(createCheckBox(issues.cost()))
                                .withIssue2(createCheckBox(issues.control()))
                                .withIssue3(createCheckBox(issues.safety()));
                    }

                    if (flags != null) {
                        row.withHasPV(createCheckBox(flags.hasPV()))
                                .withWantsPV(createCheckBox(flags.wantsPV()))
                                .withNeedsLift(createCheckBox(flags.lift()))
                                .withDeceased(createCheckBox(flags.deceased()));
                    }

                    if (volunteer != null) {
                        row.withPoster(createCheckBox(volunteer.getPoster()));
                    }

                    return row.withStreet(property.street())
                            .withErn(createRollNum(voter))
                            .build();
                })
                .collect(toList());
    }

    private String createRollNum(Voter voter) {
        return String.format("%s-%s-%s",
                voter.getPollingDistrict(), voter.getElectorNumber(), voter.getElectorSuffix());
    }

    private String createCheckBox(Boolean value) {
        if (value != null && value) {
            return "X";
        } else {
            return "";
        }
    }
}
