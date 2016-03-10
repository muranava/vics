package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.paf.dto.*;
import com.infinityworks.webapp.pdf.model.ElectorRow;
import com.infinityworks.webapp.pdf.model.ElectorRowBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static com.infinityworks.webapp.pdf.model.ElectorRowBuilder.electorRow;
import static java.util.stream.Collectors.toList;

@Component
public class PropertyToRowsConverter implements BiFunction<String, Property, List<ElectorRow>> {
    @Override
    public List<ElectorRow> apply(String wardCode, Property property) {
        return property.voters().stream()
                .map(voter -> {
                    Issues issues = voter.issues();
                    Volunteer volunteer = voter.volunteer();
                    Voting voting = voter.voting();
                    Flags flags = voter.flags();

                    ElectorRowBuilder row = electorRow()
                            .withHouse(property.house())
                            .withName(voter.lastName() + ", " + voter.firstName())
                            .withTelephone(voter.telephone());

                    if (voting != null) {
                        row.withLikelihood(normalizeScore(voting.likelihood()))
                                .withIntention(normalizeScore(voting.intention()));
                    }

                    if (issues != null) {
                        row.withIssue1(createCheckBox(issues.cost()))
                                .withIssue2(createCheckBox(issues.sovereignty()))
                                .withIssue3(createCheckBox(issues.border()));
                    }

                    if (flags != null) {
                        row.withHasPV(createCheckBox(flags.hasPV()))
                                .withWantsPV(createCheckBox(flags.wantsPV()))
                                .withNeedsLift(createCheckBox(flags.lift()))
                                .withInaccessible(createCheckBox(flags.inaccessible()))
                                .withDeceased(createCheckBox(flags.deceased()));
                    }

                    if (volunteer != null) {
                        row.withPoster(createCheckBox(volunteer.poster()));
                    }

                    return row.withStreet(property.street())
                            .withErn(createRollNum(voter))
                            .build();
                })
                .collect(toList());
    }

    private String normalizeScore(Integer value) {
        if (value == null || value == 0) {
            return "";
        } else {
            return String.valueOf(value);
        }
    }

    private String createRollNum(Voter voter) {
        return String.format("%s-%s-%s",
                voter.pollingDistrict(), voter.electorNumber(), voter.electorSuffix());
    }

    private String createCheckBox(Boolean value) {
        if (value != null && value) {
            return "X";
        } else {
            return "";
        }
    }
}
