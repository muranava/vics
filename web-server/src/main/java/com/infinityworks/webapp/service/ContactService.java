package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Ern;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.clients.paf.command.DeleteContactCommand;
import com.infinityworks.webapp.clients.paf.command.DeleteContactCommandFactory;
import com.infinityworks.webapp.clients.paf.command.RecordContactCommand;
import com.infinityworks.webapp.clients.paf.command.RecordContactCommandFactory;
import com.infinityworks.webapp.clients.paf.converter.RecordContactToPafConverter;
import com.infinityworks.webapp.clients.paf.dto.DeleteContactResponse;
import com.infinityworks.webapp.clients.paf.dto.RecordContactResponse;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final Logger log = LoggerFactory.getLogger(VoterService.class);
    private final WardService wardService;
    private final RecordContactToPafConverter recordContactToPafConverter;
    private final RecordContactCommandFactory recordContactCommandFactory;
    private final DeleteContactCommandFactory deleteContactCommandFactory;

    @Autowired
    public ContactService(WardService wardService,
                          RecordContactToPafConverter recordContactToPafConverter,
                          RecordContactCommandFactory recordContactCommandFactory,
                          DeleteContactCommandFactory deleteContactCommandFactory) {
        this.wardService = wardService;
        this.recordContactToPafConverter = recordContactToPafConverter;
        this.recordContactCommandFactory = recordContactCommandFactory;
        this.deleteContactCommandFactory = deleteContactCommandFactory;
    }

    /**
     * Adds a contact record representing the outcome of a canvassing activity
     *
     * @param user           the logged in user submitted the request
     * @param ern            the ID of the voter contacted
     * @param contactRequest the contact data
     * @return the updated contact data, or a failure object if the operation failed
     */
    public Try<RecordContactResponse> recordContact(User user,
                                                    Ern ern,
                                                    RecordContactRequest contactRequest) {
        if (!user.getWriteAccess()) {
            log.warn("User={} tried to add contact for ern={} but does not have write access", user, ern);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }
        return wardService
                .getByCode(ern.getWardCode(), user)
                .flatMap(ward -> {
                    RecordContactCommand recordContactCommand = recordContactCommandFactory.create(
                            ern.get(), recordContactToPafConverter.apply(user, contactRequest));
                    return recordContactCommand.execute();
                });
    }

    public Try<DeleteContactResponse> deleteContact(User user, Ern ern, String contactId) {
        if (!user.getWriteAccess()) {
            log.warn("User={} tried to delete contact for ern={} but does not have write access", user, ern);
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }
        return wardService
                .getByCode(ern.getWardCode(), user)
                .flatMap(ward -> {
                    DeleteContactCommand recordContactCommand = deleteContactCommandFactory.create(
                        ern.get(), contactId);
                        return recordContactCommand.execute();
                    });
    }
}
