package com.infinityworks.webapp.clients.email;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.error.PasswordNotificationFailure;
import com.infinityworks.webapp.error.SendGridApiFailure;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;
import com.sendgrid.SendGrid.Response;
import com.sendgrid.SendGridException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
class SendGridEmailClient implements EmailClient {
    private final Logger log = LoggerFactory.getLogger(SendGridEmailClient.class);
    private final SendGrid sendGrid;

    @Autowired
    public SendGridEmailClient(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    @Override
    public Try<EmailResponse> sendEmail(EmailMessage message) {
        Email email = createEmailMessage(message);
        return sendEmail(email);
    }

    private Email createEmailMessage(EmailMessage message) {
        Email email = new Email();
        email.addTo(message.to());
        email.addToName(message.name());
        email.setFrom(message.from());
        email.setSubject(message.subject());
        email.setText(message.body());
        return email;
    }

    private Try<EmailResponse> sendEmail(Email email) {
        String recipients = Arrays.toString(email.getTos());
        try {
            Response response = sendGrid.send(email);
            if (!response.getStatus()) {
                log.info(String.format("Failed to send password reset notification to %s (using SendGrid). %s", recipients, response.getMessage()));
                return Try.failure(new PasswordNotificationFailure("Failed to send password to username " + recipients));
            } else {
                log.info(String.format("Emailed password reset to %s", recipients));
                return Try.success(ImmutableEmailResponse.builder().withMessage(response.getMessage()).build());
            }
        } catch (SendGridException e) {
            log.error(String.format("Failed to contact SendGrid to send password reset notification. Recipients=%s", recipients), e);
            return Try.failure(new SendGridApiFailure("Failed to send password"));
        }
    }
}
