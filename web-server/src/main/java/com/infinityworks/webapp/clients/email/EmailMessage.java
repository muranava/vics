package com.infinityworks.webapp.clients.email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
public interface EmailMessage {
    String to();
    String name();
    String from();
    String subject();
    String body();
    String category();
}
