package com.infinityworks.webapp.pdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableAddressLabel.class)
@JsonSerialize(as = ImmutableAddressLabel.class)
public interface AddressLabel {
    String name();
    String addressLine1();
    String addressLine2();
    String postTown();
    String postCode();
}
