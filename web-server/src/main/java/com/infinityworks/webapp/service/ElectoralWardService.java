package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.infinityworks.webapp.common.lang.StringExtras.toUpperCase;

/**
 * Services to supply electoral wards and constituencies (static data)
 */
@Service
class ElectoralWardService implements WardService {

    private final WardRepository wardRepository;

    @Autowired
    public ElectoralWardService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    @Override
    public Try<List<Ward>> findByConstituencyName(String name) {
        return Try.of(() -> wardRepository.findByConstituencyNameIgnoreCase(name.toUpperCase()));
    }

    @Override
    public Try<List<Ward>> findByConstituencyNameAndWardNames(String constituencyName, List<String> wardNames) {
        return Try.of(() -> wardRepository.findByConstituencyNameAndWardNames(constituencyName.toUpperCase(), toUpperCase(wardNames)));
    }
}
