package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.paf.client.command.RecordContactCommand;
import com.infinityworks.webapp.paf.client.command.RecordContactCommandFactory;
import com.infinityworks.webapp.paf.converter.RecordContactToPafConverter;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordContactService {

    private final Logger log = LoggerFactory.getLogger(VoterService.class);
    private final WardService wardService;
    private final RecordContactToPafConverter recordContactToPafConverter;
    private final RecordContactCommandFactory recordContactCommandFactory;
    private final ErnShortFormToLongFormConverter ernEnricher;

    @Autowired
    public RecordContactService(WardService wardService,
                                RecordContactToPafConverter recordContactToPafConverter,
                                RecordContactCommandFactory recordContactCommandFactory,
                                ErnShortFormToLongFormConverter ernEnricher) {
        this.wardService = wardService;
        this.recordContactToPafConverter = recordContactToPafConverter;
        this.recordContactCommandFactory = recordContactCommandFactory;
        this.ernEnricher = ernEnricher;
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
                .flatMap(ward -> ernEnricher.apply(ward.getCode(), ern)
                        .flatMap(fullErn -> {
                            RecordContactCommand recordContactCommand = recordContactCommandFactory.create(fullErn, recordContactToPafConverter.apply(user, contactRequest));
                            return recordContactCommand.execute();
                        })
                        .map(response -> contactRequest));
    }
}
