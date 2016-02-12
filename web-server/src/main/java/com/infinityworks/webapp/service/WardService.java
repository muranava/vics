package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.repository.WardRepository;
import com.infinityworks.webapp.rest.dto.UserRestrictedWards;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Services to supply electoral wards and constituencies (static data)
 */
@Service
public class WardService {
    private final Logger log = LoggerFactory.getLogger(WardService.class);
    private final WardRepository wardRepository;
    private final ConstituencyRepository constituencyRepository;

    @Autowired
    public WardService(WardRepository wardRepository, ConstituencyRepository constituencyRepository) {
        this.wardRepository = wardRepository;
        this.constituencyRepository = constituencyRepository;
    }

    public List<Ward> findByCode(String code) {
        return wardRepository.findByCodeOrderByNameAsc(code);
    }

    public Try<UserRestrictedWards> findByConstituency(UUID id, User user) {
        log.debug("Finding constituency={} for user={}", id, user);

        Optional<Constituency> constituency = Optional.ofNullable(constituencyRepository.findOne(id));
        if (!constituency.isPresent()) {
            log.warn("Could not find constituency={} for user={}", id, user);
            return Try.failure(new NotFoundFailure("No constituency with ID " + id));
        }

        Set<Ward> wards;
        if (user.isAdmin()) {
            wards = newHashSet(wardRepository.findAll());
        } else {
            wards = user.getConstituencies().stream()
                    .map(Constituency::getWards)
                    .flatMap(Collection::stream)
                    .collect(toSet());
            wards.addAll(user.getWards());
        }

        UserRestrictedWards userRestrictedWards = new UserRestrictedWards(wards.stream()
                .filter(ward -> Objects.equals(ward.getConstituency(), constituency.get()))
                .collect(toList()));
        return Try.success(userRestrictedWards);
    }

    public UserRestrictedWards getByUser(User user) {
        if (user.isAdmin()) {
            return new UserRestrictedWards(wardRepository.findAll());
        } else {
            Set<Ward> wards = wardRepository.findByConstituencyIn(user.getConstituencies());
            wards.addAll(user.getWards());
            return new UserRestrictedWards(new ArrayList<>(wards));
        }
    }
}
