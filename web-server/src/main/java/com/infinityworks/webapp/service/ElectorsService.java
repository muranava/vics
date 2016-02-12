package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.rest.dto.TownStreets;
import com.infinityworks.webapp.service.client.PafClient;
import com.infinityworks.webapp.service.client.Property;
import com.infinityworks.webapp.service.client.VotersByStreet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Searches for electors within the given ward.
 * The elector data is retrieve from the PAF api.
 */
@Service
public class ElectorsService {
    private final Logger log = LoggerFactory.getLogger(ElectorsService.class);
    private final PafClient pafClient;
    private final WardService wardService;

    @Autowired
    public ElectorsService(PafClient pafClient,
                           WardService wardService) {
        this.pafClient = pafClient;
        this.wardService = wardService;
    }

    public Try<List<VotersByStreet>> findElectorsByStreet(TownStreets townStreets, String wardCode, User user) {
        log.debug("Finding electors by streets={} for user={}", townStreets, user);

        Optional<Ward> byCode = wardService.findByCode(wardCode).stream().findFirst();
        if (!byCode.isPresent()) {
            String msg = String.format("No ward with code=%s", wardCode);
            log.warn(msg);
            return Try.failure(new NotFoundFailure(msg));
        }

        if (!user.hasWardPermission(byCode.get())) {
            String msg = String.format("User=%s tried to access ward=%s", user, wardCode);
            log.warn(msg);
            return Try.failure(new NotAuthorizedFailure("Not Authorized"));
        }

        return pafClient.findElectorsByStreet(townStreets, wardCode);
    }

}
