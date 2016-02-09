package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Services to supply electoral wards and constituencies (static data)
 */
@Service
public class WardService {

    private final WardRepository wardRepository;
    private final ConstituencyRepository constituencyRepository;

    @Autowired
    public WardService(WardRepository wardRepository, ConstituencyRepository constituencyRepository) {
        this.wardRepository = wardRepository;
        this.constituencyRepository = constituencyRepository;
    }

    public Try<List<Ward>> findByConstituency(UUID id) {
        Optional<Constituency> constituency = Optional.ofNullable(constituencyRepository.findOne(id));
        if (constituency.isPresent()) {
            return Try.success(wardRepository.findByConstituencyOrderByNameAsc(constituency.get()));
        } else {
            return Try.failure(new NotFoundFailure("No constituency with ID " + id));
        }
    }
}
