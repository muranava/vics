package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.converter.RecordContactToPafConverter;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordContactService {

    private final Logger log = LoggerFactory.getLogger(VoterService.class);
    private final PafClient pafClient;
    private final WardService wardService;
    private final RecordContactToPafConverter recordContactToPafConverter;

    @Autowired
    public RecordContactService(PafClient pafClient,
                                WardService wardService,
                                RecordContactToPafConverter recordContactToPafConverter) {
        this.pafClient = pafClient;
        this.wardService = wardService;
        this.recordContactToPafConverter = recordContactToPafConverter;
    }

    /**
     * Adds a contact record representing the outcome of a canvassing activity
     *
     * @param user           the logged in user submitted the request
     * @param ern            the ID of the voter contacted
     * @param contactRequest the contact data
     * @return the updated contact data, or a failure object if the operation failed
     */
    public Try<RecordContactRequest> recordContact(User user, String ern, RecordContactRequest contactRequest) {
        if (!user.getWriteAccess()) {
            log.warn("User={} tried to add contact for ern={} but does not have write access", user, ern);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }
        return wardService
                .getByCode(contactRequest.getWardCode(), user)
                .flatMap(ward -> pafClient.recordContact(ern, recordContactToPafConverter.apply(user, contactRequest)))
                .map(response -> contactRequest);

    }
}
