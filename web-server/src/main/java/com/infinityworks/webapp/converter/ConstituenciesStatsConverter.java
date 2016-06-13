package com.infinityworks.webapp.converter;

import com.infinityworks.pafclient.dto.ConstituenciesStats;
import com.infinityworks.webapp.rest.dto.*;
import com.infinityworks.webapp.service.ConstituencyRegionMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class ConstituenciesStatsConverter implements Function<List<ConstituenciesStats>, StatsOverview> {

    private final ConstituencyRegionMappingService regionMappingService;

    @Autowired
    public ConstituenciesStatsConverter(ConstituencyRegionMappingService regionMappingService) {
        this.regionMappingService = regionMappingService;
    }

    @Override
    public StatsOverview apply(List<ConstituenciesStats> constituenciesStats) {
        int voters = 0;
        int canvassed = 0;
        int pledges = 0;
        int voted = 0;
        int votedPledges = 0;
        List<ConstituenciesStatsResponse> transformedStats = new ArrayList<>();

        for (ConstituenciesStats stats : constituenciesStats) {
            voters += stats.voters();
            canvassed += stats.canvassed();
            pledges += stats.pledged();
            voted += stats.voted().total();
            votedPledges += stats.voted().pledged();

            String regionName = regionMappingService.getRegionByConstituency(stats.code());

            ConstituenciesStatsResponse constituencies = ImmutableConstituenciesStatsResponse.builder()
                    .withRegion(regionName)
                    .withStats(stats)
                    .build();

            transformedStats.add(constituencies);
        }

        return ImmutableStatsOverview.builder()
                .withConstituencies(transformedStats)
                .withTotal(ImmutableOverallStats.builder()
                        .withVoters(voters)
                        .withCanvassed(canvassed)
                        .withPledges(pledges)
                        .withVoted(voted)
                        .withPledgesVoted(votedPledges)
                        .build())
                .build();
    }
}
