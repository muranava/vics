package com.infinityworks.webapp.paf.converter;

import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.paf.dto.*;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class RecordContactToPafConverter implements BiFunction<User, RecordContactRequest, com.infinityworks.webapp.paf.dto.RecordContactRequest> {
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

        return ImmutableRecordContactRequest.builder()
                .withContactType("canvass")
                .withUserId(user.getId().toString())
                .withVoting(builder)
                .withFlags(flags)
                .withUserId(user.getId().toString())
                .build();

    }
}
