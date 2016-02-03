package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Address;
import com.infinityworks.webapp.domain.Elector;
import com.infinityworks.webapp.repository.ElectorRepository;
import com.infinityworks.webapp.rest.dto.ElectorResponse;
import com.infinityworks.webapp.rest.dto.PrintElectorsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
class ElectorPrintService implements PrintService {

    private final ElectorRepository electorRepository;

    @Autowired
    public ElectorPrintService(ElectorRepository electorRepository) {
        this.electorRepository = electorRepository;
    }

    @Override
    public Try<List<ElectorResponse>> printElectors(PrintElectorsRequest request) {
        Random r = new Random();
        Try<List<Elector>> electors = Try.of(electorRepository::findAll);
        return electors.map(e -> e.stream().map(a -> {
            Address address = new Address(String.valueOf(r.nextInt(70 - 1) + 1), randStreet());
            return ElectorResponse.of(a, address);
        }).collect(toList()));
    }

    // FIXME remove fake data
    private final List<String> streets = asList("Boswell Drive", "Baker Street", "Leather Lane", "Brandon Road", "London Road");
    private String randStreet() {
        Collections.shuffle(streets);
        return streets.get(0);
    }
}
