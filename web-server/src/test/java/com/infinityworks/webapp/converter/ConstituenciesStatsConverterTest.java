package com.infinityworks.webapp.converter;

import com.infinityworks.pafclient.dto.ImmutableConstituenciesStats;
import com.infinityworks.pafclient.dto.ImmutableStatsIntention;
import com.infinityworks.pafclient.dto.ImmutableStatsVoted;
import com.infinityworks.webapp.rest.dto.ConstituenciesStatsResponse;
import com.infinityworks.webapp.service.ConstituencyRegionMappingService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConstituenciesStatsConverterTest {

    private ConstituenciesStatsConverter underTest;
    private ConstituencyRegionMappingService mappingService;

    @Before
    public void setUp() throws Exception {
        mappingService = mock(ConstituencyRegionMappingService.class);
        underTest = new ConstituenciesStatsConverter(mappingService);
    }

    @Test
    public void convertsTheConstituenciesStats() throws Exception {
        when(mappingService.getRegionByConstituency("E14000649")).thenReturn("West Midlands");

        ImmutableConstituenciesStats stats = ImmutableConstituenciesStats.builder()
                .withCanvassed(1)
                .withCode("E14000649")
                .withName("Coventry North East")
                .withIntention(ImmutableStatsIntention.builder()
                        .withProbablyRemain(1)
                        .withRemain(2)
                        .withLeave(3)
                        .withUndecided(4)
                        .withProbablyLeave(5)
                        .build())
                .withVoted(ImmutableStatsVoted.builder()
                        .withTotal(6)
                        .withPledged(7)
                        .build())
                .withPledged(8)
                .withVoters(9)
                .build();
        ConstituenciesStatsResponse response = underTest.apply(stats);

        assertThat(response.region(), is("West Midlands"));
        assertThat(response.stats(), is(stats));
    }
}