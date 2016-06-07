package com.infinityworks.pdfserver.converter;

import com.infinityworks.common.lang.StringExtras;
import com.infinityworks.pafclient.dto.*;
import com.infinityworks.pdfserver.pdf.model.ElectorRow;
import com.infinityworks.pdfserver.pdf.model.ImmutableElectorRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Maps a property (house) to a collection of voters
 */
@Component
public class PropertyToRowsConverter implements BiFunction<String, Property, List<ElectorRow>> {
    @Override
    public List<ElectorRow> apply(String wardCode, Property property) {
        return property.voters().stream()
                .map(voter -> {
                    Issues issues = voter.issues();
                    Voting voting = voter.voting();
                    Flags flags = voter.flags();
                    Info info = voter.info();

                    ImmutableElectorRow.Builder builder = ImmutableElectorRow.builder()
                            .withHouse(property.house())
                            .withName(voter.fullName());
                    createVoterAttributes(issues, voting, flags, info, builder);

                    return builder.withStreet(property.street())
                            .withErn(createRollNum(voter))
                            .build();
                })
                .collect(toList());
    }

    private void createVoterAttributes(Issues issues,
                                       Voting voting,
                                       Flags flags,
                                       Info info,
                                       ImmutableElectorRow.Builder builder) {
        if (voting != null) {
            builder.withLikelihood(normalizeScore(voting.likelihood()))
                    .withSupport(normalizeScore(voting.intention()))
                    .withHasVoted(createCheckBox(voting.hasVoted()));
        }

        if (issues != null) {
            builder.withIssue1(createCheckBox(issues.cost()))
                    .withIssue2(createCheckBox(issues.sovereignty()))
                    .withIssue3(createCheckBox(issues.border()));
        }

        if (flags != null) {
            builder.withHasPV(createCheckBox(flags.hasPV()))
                    .withWantsPV(createCheckBox(flags.wantsPV()))
                    .withPoster(createCheckBox(flags.poster()))
                    .withNeedsLift(createCheckBox(flags.lift()))
                    .withInaccessible(createCheckBox(flags.inaccessible()))
                    .withDeceased(createCheckBox(flags.deceased()));
        }

        if (info != null) {
            builder.withEmail(info.email());
            builder.withTelephone(info.telephone());
        }
    }

    private String normalizeScore(Integer value) {
        if (value == null) {
            return "";
        } else if (value == 0) {
            return "N/A";
        } else {
            return String.valueOf(value);
        }
    }

    private String createRollNum(Voter voter) {
        return Stream.of(voter.pollingDistrict(), voter.electorNumber(), voter.electorSuffix())
                .filter(e -> !StringExtras.isNullOrEmpty(e))
                .collect(joining("-"));
    }

    private String createCheckBox(Boolean value) {
        if (value != null && value) {
            return "X";
        } else {
            return "";
        }
    }
}
