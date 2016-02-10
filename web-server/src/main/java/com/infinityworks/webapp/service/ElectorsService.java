package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.service.client.PafClient;
import com.infinityworks.webapp.service.client.PafRecord;
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
    public ElectorsService(PafClient pafClient, WardService wardService) {
        this.pafClient = pafClient;
        this.wardService = wardService;
    }

    public Try<List<PafRecord>> findElectorsByWard(String wardCode) {
        log.debug("Finding electors by ward {}", wardCode);

        Optional<Ward> wards = wardService.findByCode(wardCode).stream().findFirst();
        if (!wards.isPresent()) {
            log.warn("Ward {} not found", wardCode);
            return Try.failure(new NotFoundFailure("No wards with code " + wardCode));
        }

        Try<List<PafRecord>> electorsByWard = pafClient.findElectorsByWard(wards.get().getCode());
        electorsByWard.accept(
                failure -> log.error("Failed to retrieve electors for wardCode {}", wardCode),
                electors -> log.debug("Retrieved {} electors from PAF", electors.size()));

        return electorsByWard;
    }
}
