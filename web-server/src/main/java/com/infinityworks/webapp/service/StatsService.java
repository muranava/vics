package com.infinityworks.webapp.service;

import com.infinityworks.webapp.converter.MostCanvassedQueryConverter;
import com.infinityworks.webapp.converter.TopCanvasserQueryConverter;
import com.infinityworks.webapp.repository.RecordContactLogRepository;
import com.infinityworks.webapp.repository.StatsJdbcRepository;
import com.infinityworks.webapp.repository.StatsRepository;
import com.infinityworks.webapp.rest.dto.AllStatsResponse;
import com.infinityworks.webapp.rest.dto.ImmutableAllStatsResponse;
import com.infinityworks.webapp.rest.dto.StatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class StatsService {

    private final StatsRepository repository;
    private final RecordContactLogRepository recordContactLogRepository;
    private final StatsJdbcRepository statsJdbcRepository;
    private final TopCanvasserQueryConverter topCanvasserQueryConverter;
    private final MostCanvassedQueryConverter mostCanvassedQueryConverter;
    private static final int LIMIT = 6;

    @Autowired
    public StatsService(StatsRepository repository,
                        RecordContactLogRepository recordContactLogRepository, StatsJdbcRepository statsJdbcRepository,
                        TopCanvasserQueryConverter topCanvasserQueryConverter,
                        MostCanvassedQueryConverter mostCanvassedQueryConverter) {
        this.repository = repository;
        this.recordContactLogRepository = recordContactLogRepository;
        this.statsJdbcRepository = statsJdbcRepository;
        this.topCanvasserQueryConverter = topCanvasserQueryConverter;
        this.mostCanvassedQueryConverter = mostCanvassedQueryConverter;
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
