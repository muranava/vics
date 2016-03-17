package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.rest.dto.UserRestrictedConstituencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

/**
 * Searches for electors within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class ConstituencyService {
    private final Logger log = LoggerFactory.getLogger(ConstituencyService.class);
    private final ConstituencyRepository constituencyRepository;
    private final UserRepository userRepository;

    private final Object lock = new Object();

    @Autowired
    public ConstituencyService(ConstituencyRepository constituencyRepository,
                               UserRepository userRepository) {
        this.constituencyRepository = constituencyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Gets the constituencies a user can access. This is the sum of the constituencies from
     * associated wards and directly associated wards
     *
     * @param permissible the user to get constituencies
     * @return the constituencies visible to a user (however the user may not have access to all wards
     * in the constituencies)
     */
    public UserRestrictedConstituencies getVisibleConstituenciesByUserWithWardContext(User permissible) {
        Set<Constituency> wardConstituencies = permissible.getWards()
                .stream()
                .map(Ward::getConstituency)
                .collect(toSet());

        wardConstituencies.addAll(permissible.getConstituencies());
        return new UserRestrictedConstituencies(wardConstituencies);
    }

    public Try<List<Constituency>> constituenciesByName(User permissible, String name, int limit) {
        if (!permissible.isAdmin()) {
            log.error("Non admin attempted to find all constituencies by name. user={}", permissible);
            return Try.failure(new NotAuthorizedFailure("Forbidden content"));
        }
        List<Constituency> constituencies = constituencyRepository.findByNameIgnoreCaseContainingOrderByNameAsc(name, new PageRequest(0, limit));
        return Try.success(constituencies);
    }

    public Try<Constituency> getByCode(String code) {
        Optional<Constituency> constituency = constituencyRepository.findOneByCode(code);
        if (constituency.isPresent()) {
            return Try.success(constituency.get());
        } else {
            return Try.failure(new NotFoundFailure("No constituency={}", code));
        }
    }
}
