package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.converter.ElectorWithAddressConverter;
import com.infinityworks.webapp.repository.EnrichedElectorRepository;
import com.infinityworks.webapp.rest.dto.ElectorResponse;
import com.infinityworks.webapp.rest.dto.ElectorsByWardsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ElectorService implements PrintService {

    private final EnrichedElectorRepository enrichedElectorRepository;
    private final ElectorWithAddressConverter electorWithAddressConverter;

    @Autowired
    public ElectorService(EnrichedElectorRepository enrichedElectorRepository,
                          ElectorWithAddressConverter electorWithAddressConverter) {
        this.enrichedElectorRepository = enrichedElectorRepository;
        this.electorWithAddressConverter = electorWithAddressConverter;
    }

    @Override
    public Try<List<ElectorResponse>> findPafEnrichedElectors(ElectorsByWardsRequest request) {
        throw new UnsupportedOperationException("NYI");
    }

    @Override
    public Try<List<ElectorResponse>> findLocalElectors(ElectorsByWardsRequest request) {
        return Try.of(() ->
                enrichedElectorRepository.findByWardCodeInOrderByWardCodeAscStreetAscHouseAsc(request.getWardCodes()))
                .map(electorWithAddressConverter::apply);
    }
}
