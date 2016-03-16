package com.infinityworks.webapp.paf.converter;

import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.paf.dto.*;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class RecordContactToPafConverter implements BiFunction<User, RecordContactRequest, com.infinityworks.webapp.paf.dto.RecordContactRequest> {
    private static final String CONTACT_TYPE = "canvass";

    @Override
    public com.infinityworks.webapp.paf.dto.RecordContactRequest apply(User user, RecordContactRequest contactRequest) {
        Voting builder = ImmutableVoting.builder()
                .withIntention(contactRequest.getIntention())
                .withLikelihood(contactRequest.getLikelihood())
                .build();

        Flags flags = ImmutableFlags.builder()
                .withDeceased(contactRequest.getDeceased())
                .withHasPV(contactRequest.getHasPV())
                .withWantsPV(contactRequest.getWantsPV())
                .withInaccessible(contactRequest.getInaccessible())
                .withPoster(contactRequest.getPoster())
                .withLift(contactRequest.getLift())
                .build();

        Issues issues = ImmutableIssues.builder()
                .withBorder(contactRequest.getBorder())
                .withCost(contactRequest.getCost())
                .withSovereignty(contactRequest.getSovereignty())
                .build();

        Info info = ImmutableInfo.builder()
                .withTelephone(contactRequest.getTelephone())
                .withEmail(contactRequest.getEmail())
                .build();

        return ImmutableRecordContactRequest.builder()
                .withContactType(CONTACT_TYPE)
                .withUserId(user.getId().toString())
                .withVoting(builder)
                .withFlags(flags)
                .withUserId(user.getId().toString())
                .withIssues(issues)
                .withInfo(info)
                .build();
    }
}
