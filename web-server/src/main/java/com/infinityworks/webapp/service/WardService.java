package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.converter.WardSummaryConverter;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Permissible;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import com.infinityworks.webapp.repository.WardRepository;
import com.infinityworks.webapp.rest.dto.UserRestrictedWards;
import com.infinityworks.webapp.rest.dto.WardSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Services to supply electoral wards and constituencies (static data)
 */
@Service
public class WardService {
    private final Logger log = LoggerFactory.getLogger(WardService.class);
    private final WardSummaryConverter wardSummaryConverter;
    private final WardRepository wardRepository;
    private final ConstituencyRepository constituencyRepository;

    @Autowired
    public WardService(WardSummaryConverter wardSummaryConverter,
                       WardRepository wardRepository,
                       ConstituencyRepository constituencyRepository) {
        this.wardSummaryConverter = wardSummaryConverter;
        this.wardRepository = wardRepository;
        this.constituencyRepository = constituencyRepository;
    }

    public Try<Ward> getByCode(String wardCode, Permissible permissible) {
        Optional<Ward> byWard = wardRepository.findByCode(wardCode);
        if (!byWard.isPresent()) {
            String msg = String.format("No ward with code=%s", wardCode);
            log.warn(msg);
            return Try.failure(new NotFoundFailure(msg));
        } else {
            Ward ward = byWard.get();
            if (!permissible.hasWardPermission(ward)) {
                String msg = String.format("User=%s tried to access ward=%s without permission", permissible, wardCode);
                log.warn(msg);
                return Try.failure(new NotAuthorizedFailure("Not Authorized"));
            } else {
                return Try.success(ward);
            }
        }
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

        Set<Ward> wards = user.getConstituencies().stream()
                .map(Constituency::getWards)
                .flatMap(Collection::stream)
                .collect(toSet());
        wards.addAll(user.getWards());

        UserRestrictedWards userRestrictedWards = new UserRestrictedWards(wards.stream()
                .filter(ward -> Objects.equals(ward.getConstituency(), constituency.get()))
                .collect(toList()));
        return Try.success(userRestrictedWards);
    }

    public UserRestrictedWards getByUser(User user) {
        log.debug("Getting wards by user={}", user);

        Set<Ward> wards = wardRepository.findByConstituencyIn(user.getConstituencies());
        wards.addAll(user.getWards());
        return new UserRestrictedWards(new ArrayList<>(wards));
    }

    public List<WardSummary> getSummaryByUser(User user) {
        log.debug("Getting wards summary by user={}", user);
        Set<Ward> wards = wardRepository.findByConstituencyIn(user.getConstituencies());
        wards.addAll(user.getWards());
        return wards.stream().map(wardSummaryConverter).collect(toList());
    }

    public Try<List<Ward>> getAllByName(User user, String name, int limit) {
        if (user.isAdmin()) {
            return Try.success(wardRepository.findByNameIgnoreCaseContainingOrderByNameAsc(name, new PageRequest(0, limit)));
        } else {
            log.warn("Non-admin user={} tried to get all wards", user);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }
    }

    public Try<UserRestrictedWards> searchByUserAndName(User user, String wardName, int limit) {
        log.debug("Getting wards by user={}, wardName={} limit={}", user, wardName, limit);

        Set<Ward> wards = wardRepository.searchByUsernameAndWardName(user.getUsername(), wardName.toUpperCase(), limit);

        log.debug("Found {} wards for user={}", wards, user);
        return Try.success(new UserRestrictedWards(new ArrayList<>(wards)));
    }
}
