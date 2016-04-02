package com.infinityworks.webapp.service;

import com.infinityworks.webapp.converter.MostCanvassedQueryConverter;
import com.infinityworks.webapp.converter.TopCanvasserQueryConverter;
import com.infinityworks.webapp.repository.StatsRepository;
import com.infinityworks.webapp.rest.dto.StatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class StatsService {

    private final StatsRepository repository;
    private final TopCanvasserQueryConverter topCanvasserQueryConverter;
    private final MostCanvassedQueryConverter mostCanvassedQueryConverter;
    private static final int LIMIT = 7;

    @Autowired
    public StatsService(StatsRepository repository,
                        TopCanvasserQueryConverter topCanvasserQueryConverter,
                        MostCanvassedQueryConverter mostCanvassedQueryConverter) {
        this.repository = repository;
        this.topCanvasserQueryConverter = topCanvasserQueryConverter;
        this.mostCanvassedQueryConverter = mostCanvassedQueryConverter;
    }

    public List<StatsResponse> topCanvassersStats() {
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
}
