package com.infinityworks.webapp.notifications;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.email.EmailClient;
import com.infinityworks.webapp.clients.email.EmailMessage;
import com.infinityworks.webapp.clients.email.EmailResponse;
import com.infinityworks.webapp.clients.email.ImmutableEmailMessage;
import com.infinityworks.webapp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewAccountNotifier {

    private final EmailClient emailClient;
    private static final String MESSAGE_TEMPLATE = "";

    @Autowired
    public NewAccountNotifier(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public Try<EmailResponse> sendAccountCreationInformation(User user) {
        EmailMessage message = ImmutableEmailMessage.builder()
                .withName(user.getFirstName() + " " + user.getLastName())
                .withTo(user.getUsername())
                .withBody(MESSAGE_TEMPLATE)
                .withSubject("Vics account")
                .withFrom("vicssupport@voteleave.uk")
                .build();
        return emailClient.sendEmail(message);
    }
}
