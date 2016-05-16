package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.converter.PafStreetRequestConverter;
import com.infinityworks.webapp.clients.paf.dto.*;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.Flags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class GetFilteredVotersRequestBuilder implements Function<ElectorsByStreetsRequest, GotvVoterRequest> {
    private final PafStreetRequestConverter streetConverter;

    @Autowired
    public GetFilteredVotersRequestBuilder(PafStreetRequestConverter streetConverter) {
        this.streetConverter = streetConverter;
    }

    @Override
    public GotvVoterRequest apply(ElectorsByStreetsRequest electorsByStreetsRequest) {
        Flags flagsRequest = electorsByStreetsRequest.getFlags();

        ImmutableGotvVoterRequest.Builder requestBuilder = ImmutableGotvVoterRequest.builder();
        ImmutableGotvFilter.Builder gotvFilter = ImmutableGotvFilter.builder();
        if (flagsRequest != null) {
            GotvFilterFlags flags = ImmutableGotvFilterFlags.builder()
                    .withHasPV(flagsRequest.getHasPV())
                    .withWantsPV(flagsRequest.getWantsPv())
                    .withLift(flagsRequest.getNeedsLift())
                    .withPoster(flagsRequest.getPoster())
                    .withInaccessible(flagsRequest.getInaccessible())
                    .build();

            gotvFilter.withFlags(flags);

            if (flagsRequest.getIntentionFrom() != null) {
                GotvVotingCriteria votingCriteria = ImmutableGotvVotingCriteria.builder()
                        .withIntention(ImmutableRange.builder()
                                .withMin(flagsRequest.getIntentionFrom())
                                .withMax(flagsRequest.getIntentionTo()).build())
                        .withLikelihood(ImmutableRange.builder()
                                .withMin(flagsRequest.getLikelihoodFrom())
                                .withMax(flagsRequest.getLikelihoodTo()).build())
                        .build();
                gotvFilter.withVoting(votingCriteria);
            }
        }

        List<PafStreetRequest> streets = electorsByStreetsRequest.getStreets().stream()
                .map(streetConverter)
                .collect(toList());

        requestBuilder.withStreets(streets);
        requestBuilder.withFilter(gotvFilter.build());

        return requestBuilder.build();
    }
}
