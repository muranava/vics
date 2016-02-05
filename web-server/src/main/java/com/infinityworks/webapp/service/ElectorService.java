package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.converter.ElectorConverter;
import com.infinityworks.webapp.converter.ElectorWithAddressConverter;
import com.infinityworks.webapp.domain.Address;
import com.infinityworks.webapp.repository.ElectorRepository;
import com.infinityworks.webapp.repository.EnrichedElectorRepository;
import com.infinityworks.webapp.rest.dto.ElectorResponse;
import com.infinityworks.webapp.rest.dto.ElectorsByWardsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ElectorService implements PrintService {

    private final ElectorRepository electorRepository;
    private final EnrichedElectorRepository enrichedElectorRepository;
    private final ElectorWithAddressConverter electorWithAddressConverter;
    private final ElectorConverter electorConverter;

    @Autowired
    public ElectorService(ElectorRepository electorRepository,
                          EnrichedElectorRepository enrichedElectorRepository,
                          ElectorWithAddressConverter electorWithAddressConverter,
                          ElectorConverter electorConverter) {
        this.electorRepository = electorRepository;
        this.enrichedElectorRepository = enrichedElectorRepository;
        this.electorWithAddressConverter = electorWithAddressConverter;
        this.electorConverter = electorConverter;
    }

    @Override
    public Try<List<ElectorResponse>> findPafEnrichedElectors(ElectorsByWardsRequest request) {
        return Try.of(() ->
                electorRepository.findByWardCodeIn(request.getWardCodes()))
                .map(electors -> electorConverter.apply(electors, new Address("32a", "Something"))); // FIXME add data from PAF
    }

    @Override
    public Try<List<ElectorResponse>> findLocalElectors(ElectorsByWardsRequest request) {
        return Try.of(() ->
                enrichedElectorRepository.findByWardCodeInOrderByWardCodeAscStreetAscHouseAsc(request.getWardCodes()))
                .map(electorWithAddressConverter::apply);
    }
}
