package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.DeleteContactCommand;
import com.infinityworks.webapp.clients.paf.command.DeleteContactCommandFactory;
import com.infinityworks.webapp.clients.paf.command.RecordContactCommandFactory;
import com.infinityworks.webapp.clients.paf.converter.RecordContactToPafConverter;
import com.infinityworks.webapp.clients.paf.dto.DeleteContactResponse;
import com.infinityworks.webapp.clients.paf.dto.RecordContactResponse;
import com.infinityworks.webapp.domain.Ern;
import com.infinityworks.webapp.domain.RecordContactLog;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecordContactService {

    private final Logger log = LoggerFactory.getLogger(VoterService.class);
    private final WardService wardService;
    private final RecordContactToPafConverter recordContactToPafConverter;
    private final RecordContactCommandFactory recordContactCommandFactory;
    private final DeleteContactCommandFactory deleteContactCommandFactory;
    private final RecordContactLogService recordContactLogService;

    @Autowired
    public RecordContactService(WardService wardService,
                                RecordContactToPafConverter recordContactToPafConverter,
                                RecordContactCommandFactory recordContactCommandFactory,
                                DeleteContactCommandFactory deleteContactCommandFactory,
                                RecordContactLogService recordContactLogService) {
        this.wardService = wardService;
        this.recordContactToPafConverter = recordContactToPafConverter;
        this.recordContactCommandFactory = recordContactCommandFactory;
        this.deleteContactCommandFactory = deleteContactCommandFactory;
        this.recordContactLogService = recordContactLogService;
    }

    /**
     * Adds a contact record representing the outcome of a canvassing activity
     *
     * @param user           the logged in user submitted the request
     * @param ern            the ID of the voter contacted
     * @param contactRequest the contact data
     * @return the updated contact data, or a failure object if the operation failed
     */
    public Try<com.infinityworks.webapp.rest.dto.RecordContactResponse> recordContact(User user,
                                              Ern ern,
                                              RecordContactRequest contactRequest) {
        if (!user.getWriteAccess()) {
            log.warn("User={} tried to add contact for ern={} but does not have write access", user, ern);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        } else {
            return wardService
                    .getByCode(ern.getWardCode(), user)
                    .flatMap(ward -> {
                        com.infinityworks.webapp.clients.paf.dto.RecordContactRequest pafRequest = recordContactToPafConverter.apply(user, contactRequest);
                        Try<RecordContactResponse> execute = recordContactCommandFactory.create(ern.get(), pafRequest).execute();
                        return execute
                                .map(response -> {
                                    RecordContactLog recordContactLog = new RecordContactLog(user, ward, ern.get());
                                    recordContactLogService.logRecordContactAsync(recordContactLog);

                                    log.info("User={} recorded contact for ern={}", user, ern.get());
                                    return new com.infinityworks.webapp.rest.dto.RecordContactResponse(recordContactLog.getId(), ern, response.id());
                                });
                    });
        }

    }

    public Try<DeleteContactResponse> deleteContact(User user, Ern ern, UUID contactId, UUID localId) {
        if (!user.getWriteAccess()) {
            log.warn("User={} tried to delete contact for ern={} but does not have write access", user, ern);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        } else {
            return wardService
                    .getByCode(ern.getWardCode(), user)
                    .flatMap(ward -> {
                        DeleteContactCommand recordContactCommand = deleteContactCommandFactory.create(ern.get(), contactId);
                        return recordContactCommand.execute();
                    }).map(deleteResponse -> {

                        log.info("User={} deleted recorded contact for ern={}", user, ern);
                        recordContactLogService.deleteRecordContactAsync(localId);
                        return deleteResponse;
                    });
        }

    }
}
