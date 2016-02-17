package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.rest.dto.UserRestrictedConstituencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Searches for electors within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class ConstituencyService {
    private final Logger log = LoggerFactory.getLogger(ConstituencyService.class);
    private final ConstituencyRepository constituencyRepository;

    @Autowired
    public ConstituencyService(ConstituencyRepository constituencyRepository) {
        this.constituencyRepository = constituencyRepository;
    }

    /**
     * Gets the constituencies a user can access. This is the sum of the constituencies from
     * associated wards and directly associated wards
     *
     * @param user the user to get constituencies
     * @return the constituencies visible to a user (however the user may not have access to all wards
     * in the constituencies)
     */
    public UserRestrictedConstituencies getVisibleConstituenciesByUserWithWardContext(User user) {
        if (user.isAdmin()) {
            return new UserRestrictedConstituencies(new HashSet<>(constituencyRepository.findAll()));
        } else {
            Set<Constituency> wardConstituencies = user.getWards()
                    .stream()
                    .map(Ward::getConstituency)
                    .collect(toSet());

            wardConstituencies.addAll(user.getConstituencies());
            return new UserRestrictedConstituencies(wardConstituencies);
        }
    }

    public Try<List<Constituency>> constituenciesByName(User user, String name, int limit) {
        if (!user.isAdmin()) {
            log.error("Non admin attempted to find all constituencies by name. user={}", user);
            return Try.failure(new NotAuthorizedFailure("Forbidden content"));
        }
        List<Constituency> constituencies = constituencyRepository.findByNameIgnoreCaseContainingOrderByNameAsc(name, new PageRequest(0, limit));
        return Try.success(constituencies);
    }
}
