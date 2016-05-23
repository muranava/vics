package com.infinityworks.webapp.pdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Joiner;
import com.sun.istack.internal.Nullable;
import org.immutables.value.Value;

import static com.infinityworks.common.lang.StringExtras.isNullOrEmpty;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*")
@JsonDeserialize(as = ImmutableAddressLabel.class)
@JsonSerialize(as = ImmutableAddressLabel.class)
public interface AddressLabel {
    String name();

    String addressLine1();

    @Nullable String addressLine2();

    String postTown();

    String postCode();

    default String printFormat() {
        return Joiner.on("\n")
                .skipNulls()
                .join(name(),
                        addressLine1(),
                        isNullOrEmpty(addressLine2()) ? null : addressLine2(),
                        postTown(),
                        postCode());
    }
}
