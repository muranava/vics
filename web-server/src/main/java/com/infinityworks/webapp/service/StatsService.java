package com.infinityworks.webapp.service;

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
import com.infinityworks.webapp.rest.dto.AllStatsResponse;
import com.infinityworks.webapp.rest.dto.ImmutableAllStatsResponse;
import com.infinityworks.webapp.rest.dto.StatsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    public StatsService(StatsRepository repository,
                        RecordContactLogRepository recordContactLogRepository,
                        StatsJdbcRepository statsJdbcRepository,
                        TopCanvasserQueryConverter topCanvasserQueryConverter,
                        MostCanvassedQueryConverter mostCanvassedQueryConverter,
                        WardStatsCommandFactory wardStatsCommandFactory,
                        ConstituencyStatsCommandFactory constituencyStatsCommandFactory,
                        WardService wardService, ConstituencyService constituencyService) {
        this.repository = repository;
        this.recordContactLogRepository = recordContactLogRepository;
        this.statsJdbcRepository = statsJdbcRepository;
        this.topCanvasserQueryConverter = topCanvasserQueryConverter;
        this.mostCanvassedQueryConverter = mostCanvassedQueryConverter;
        this.wardStatsCommandFactory = wardStatsCommandFactory;
        this.constituencyStatsCommandFactory = constituencyStatsCommandFactory;
        this.wardService = wardService;
        this.constituencyService = constituencyService;
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

    public List<Object[]> recordContactByDate() {
        return repository.countRecordContactsByDate();
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

    /**
     * TODO combine into a single query
     */
    public AllStatsResponse allStats() {
        return ImmutableAllStatsResponse.builder()
                .withCanvassedThisWeek(canvassedPastNDays(LIMIT))
                .withTopWards(mostCanvassedWards())
                .withTopConstituencies(mostCanvassedConstituencies())
                .withTopCanvassers(topCanvassers())
                .withTotalContacts(recordContactLogRepository.count())
                .withRecordContactByDate(recordContactByDate())
                .build();
    }
}
