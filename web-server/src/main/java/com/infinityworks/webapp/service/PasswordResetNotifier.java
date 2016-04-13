package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.email.EmailClient;
import com.infinityworks.webapp.clients.email.EmailMessage;
import com.infinityworks.webapp.clients.email.EmailResponse;
import com.infinityworks.webapp.clients.email.ImmutableEmailMessage;
import com.infinityworks.webapp.config.AppProperties;
import com.infinityworks.webapp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetNotifier {

    private final EmailClient emailClient;
    private final String endpointTemplate;
    private static final String MESSAGE_TEMPLATE = "You requested a password reset, please visit %s to confirm.";

    @Autowired
    public PasswordResetNotifier(EmailClient emailClient, AppProperties properties) {
        this.emailClient = emailClient;
        this.endpointTemplate = properties.getPasswordResetEndpoint() + "/%s";
    }

    public Try<EmailResponse> sendPasswordResetNotification(User user, String token) {
        String resetEndpoint = String.format(endpointTemplate, token);
        EmailMessage message = ImmutableEmailMessage.builder()
                .withName(user.getFirstName() + " " + user.getLastName())
                .withTo(user.getUsername())
                .withBody(String.format(MESSAGE_TEMPLATE, resetEndpoint))
                .withSubject("Vics account password reset notification")
                .withFrom("vicssupport@voteleave.uk")
                .build();
        return emailClient.sendEmail(message);
    }
}
