package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.pdfgen.DocumentBuilder;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.error.NotAuthorizedFailure;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.pdf.PDFRenderer;
import com.infinityworks.webapp.rest.dto.RecordContactRequest;
import com.infinityworks.webapp.service.client.PafClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecordContactService {

    private final Logger log = LoggerFactory.getLogger(ElectorsService.class);
    private final PafClient pafClient;
    private final WardService wardService;

    @Autowired
    public RecordContactService(PafClient pafClient,
                                WardService wardService) {
        this.pafClient = pafClient;
        this.wardService = wardService;
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

        Optional<Ward> byCode = wardService.findByCode(contactRequest.getWardCode()).stream().findFirst();
        if (!byCode.isPresent()) {
            return Try.failure(new NotFoundFailure("No ward with code " + contactRequest.getWardCode()));
        }

        if (!user.hasWardPermission(byCode.get())) {
            return Try.failure(new NotAuthorizedFailure("Forbidden"));
        }

        return pafClient.recordContact(ern, contactRequest);
    }
}
