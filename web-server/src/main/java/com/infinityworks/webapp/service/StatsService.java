package com.infinityworks.webapp.service;

import com.google.common.collect.Maps;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.ConstituencyStatsCommand;
import com.infinityworks.webapp.clients.paf.command.ConstituencyStatsCommandFactory;
import com.infinityworks.webapp.clients.paf.command.WardStatsCommand;
import com.infinityworks.webapp.clients.paf.command.WardStatsCommandFactory;
import com.infinityworks.webapp.clients.paf.dto.ConstituencyStats;
import com.infinityworks.webapp.clients.paf.dto.WardStats;
import com.infinityworks.webapp.converter.MostCanvassedQueryConverter;
import com.infinityworks.webapp.converter.TopCanvasserQueryConverter;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.repository.RecordContactLogRepository;
import com.infinityworks.webapp.repository.StatsJdbcRepository;
import com.infinityworks.webapp.repository.StatsRepository;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.rest.dto.ImmutableLeaderboardStatsResponse;
import com.infinityworks.webapp.rest.dto.LeaderboardStatsResponse;
import com.infinityworks.webapp.rest.dto.StatsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class StatsService {

    private static final Logger log = LoggerFactory.getLogger(StatsService.class);

    private final StatsRepository repository;
    private final RecordContactLogRepository recordContactLogRepository;
    private final StatsJdbcRepository statsJdbcRepository;
    private final TopCanvasserQueryConverter topCanvasserQueryConverter;
    private final MostCanvassedQueryConverter mostCanvassedQueryConverter;
    private final WardStatsCommandFactory wardStatsCommandFactory;
    private final ConstituencyStatsCommandFactory constituencyStatsCommandFactory;
    private static final int LIMIT = 6;
    private final WardService wardService;
    private final ConstituencyService constituencyService;
    private final UserRepository userRepository;

    @Autowired
    public StatsService(StatsRepository repository,
                        RecordContactLogRepository recordContactLogRepository,
                        StatsJdbcRepository statsJdbcRepository,
                        TopCanvasserQueryConverter topCanvasserQueryConverter,
                        MostCanvassedQueryConverter mostCanvassedQueryConverter,
                        WardStatsCommandFactory wardStatsCommandFactory,
                        ConstituencyStatsCommandFactory constituencyStatsCommandFactory,
                        WardService wardService, ConstituencyService constituencyService,
                        UserRepository userRepository) {
        this.repository = repository;
        this.recordContactLogRepository = recordContactLogRepository;
        this.statsJdbcRepository = statsJdbcRepository;
        this.topCanvasserQueryConverter = topCanvasserQueryConverter;
        this.mostCanvassedQueryConverter = mostCanvassedQueryConverter;
        this.wardStatsCommandFactory = wardStatsCommandFactory;
        this.constituencyStatsCommandFactory = constituencyStatsCommandFactory;
        this.wardService = wardService;
        this.constituencyService = constituencyService;
        this.userRepository = userRepository;
    }

    public List<StatsResponse> topCanvassers() {
        return repository.countRecordContactByUser(LIMIT)
                .stream()
                .map(topCanvasserQueryConverter)
                .collect(toList());
    }

    public List<StatsResponse> mostCanvassedWards() {
        return repository.countMostRecordContactByWard(LIMIT)
                .stream()
                .map(mostCanvassedQueryConverter)
                .collect(toList());
    }

    public List<StatsResponse> mostCanvassedConstituencies() {
        return repository.countMostRecordContactByConstituency(LIMIT)
                .stream()
                .map(mostCanvassedQueryConverter)
                .collect(toList());
    }

    public int canvassedPastNDays(int days) {
        return statsJdbcRepository.countCanvassedPastDays(days);
    }

    public List<Object[]> countRecordContactsByDateAndConstituency(String constituencyCode) {
        return repository.countRecordContactsByDateAndConstituency(constituencyCode);
    }

    public List<Object[]> countRecordContactsByDateAndWard(String wardCode) {
        return repository.countRecordContactsByDateAndWard(wardCode);
    }

    public Try<List<Object[]>> countUsersByRegion(User user) {
        if (!user.isAdmin()) {
            log.warn("Non admin tried to retrieve all users. User={}", user);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        } else {
            return Try.success(repository.countUsersByRegion());
        }
    }

    public Try<WardStats> wardStats(User user, String wardCode) {
        return wardService.getByCode(wardCode, user)
                .flatMap(ward -> {
                    WardStatsCommand wardStatsCommand = wardStatsCommandFactory.create(ward.getCode());
                    return wardStatsCommand.execute();
                });
    }

    public Try<ConstituencyStats> constituencyStats(User user, String constituencyCode) {
        return constituencyService.getByCodeRestrictedByAssociation(constituencyCode, user)
                .flatMap(constituency -> {
                    ConstituencyStatsCommand constituencyStatsCommand = constituencyStatsCommandFactory.create(constituency.getCode());
                    return constituencyStatsCommand.execute();
                });
    }

    public Try<Map<String, Integer>> adminCounts(User user) {
        if (!user.isAdmin()) {
            return Try.failure(new NotAuthorizedFailure("Not authorized"));
        } else {
            Map<String, Integer> counts = Maps.newHashMap();
            counts.put("canvassedThisWeek", canvassedPastNDays(LIMIT));
            counts.put("totalCanvassed", (int) recordContactLogRepository.count());
            counts.put("users", (int) userRepository.count());
            return Try.success(counts);
        }
    }

    public LeaderboardStatsResponse leaderboardStats() {
        return ImmutableLeaderboardStatsResponse.builder()
                .withTopWards(mostCanvassedWards())
                .withTopConstituencies(mostCanvassedConstituencies())
                .withTopCanvassers(topCanvassers())
                .build();
    }
}
