package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.converter.WardSummaryConverter;
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

    /**
     * Finds the wards a user has access to (directly allocated wards including wards contained by allocated
     * constituencies), filtered by the given constituency. It would be more optimal to implement this at
     * the repository level.
     *
     * @param constituencyID constituency to filter accessible wards by
     * @param user           the current user
     * @return unique wards available to the user filtered by the given constituency
     */
    public Try<UserRestrictedWards> findByConstituency(UUID constituencyID, User user) {
        log.debug("Finding accessible wards for constituency={} user={}", constituencyID, user);

        return Optional.ofNullable(constituencyRepository.findOne(constituencyID))
                .map(constituency -> {
                    Set<Ward> wards = user.getAccessibleWards();
                    List<Ward> collect = wards.stream()
                            .filter(ward -> Objects.equals(ward.getConstituency(), constituency))
                            .collect(toList());
                    UserRestrictedWards userRestrictedWards = new UserRestrictedWards(collect);
                    return Try.success(userRestrictedWards);
                }).orElseGet(() -> {
                    log.warn("Could not find constituency={} for user={}", constituencyID, user);
                    return Try.failure(new NotFoundFailure("No constituency with ID " + constituencyID));
                });
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
            List<Ward> wards = wardRepository.findByNameIgnoreCaseContainingOrderByNameAsc(name, new PageRequest(0, limit));
            return Try.success(wards);
        } else {
            log.warn("Non-admin user={} tried to get all wards", user);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }
    }
}
