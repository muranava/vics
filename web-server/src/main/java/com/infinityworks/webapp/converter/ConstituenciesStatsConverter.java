package com.infinityworks.webapp.converter;

import com.infinityworks.pafclient.dto.ConstituenciesStats;
import com.infinityworks.webapp.rest.dto.ConstituenciesStatsResponse;
import com.infinityworks.webapp.rest.dto.ImmutableConstituenciesStatsResponse;
import com.infinityworks.webapp.service.ConstituencyRegionMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ConstituenciesStatsConverter implements Function<ConstituenciesStats, ConstituenciesStatsResponse> {

    private final ConstituencyRegionMappingService regionMappingService;

    @Autowired
    public ConstituenciesStatsConverter(ConstituencyRegionMappingService regionMappingService) {
        this.regionMappingService = regionMappingService;
    }

    @Override
    public ConstituenciesStatsResponse apply(ConstituenciesStats constituenciesStats) {
        String regionName = regionMappingService.getRegionByConstituency(constituenciesStats.code());
        return ImmutableConstituenciesStatsResponse.builder()
                .withRegion(regionName)
                .withStats(constituenciesStats)
                .build();
    }
}
