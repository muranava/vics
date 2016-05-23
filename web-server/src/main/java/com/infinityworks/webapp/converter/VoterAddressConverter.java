package com.infinityworks.webapp.converter;

import com.infinityworks.common.lang.StringExtras;
import com.infinityworks.webapp.clients.paf.dto.Property;
import com.infinityworks.webapp.clients.paf.dto.PropertyResponse;
import com.infinityworks.webapp.pdf.AddressLabel;
import com.infinityworks.webapp.pdf.ImmutableAddressLabel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class VoterAddressConverter implements Function<PropertyResponse, List<AddressLabel>> {

    @Override
    public List<AddressLabel> apply(PropertyResponse propertyResponse) {
        return propertyResponse.response()
                .stream()
                .flatMap(Collection::stream)
                .map(PropertyToVotersFn.INSTANCE)
                .flatMap(Collection::stream)
                .collect(toList());
    }
}

enum PropertyToVotersFn implements Function<Property, List<AddressLabel>> {
    INSTANCE {
        @Override
        public List<AddressLabel> apply(Property property) {
            return property.voters().stream()
                    .map(voter -> {
                                ImmutableAddressLabel.Builder builder = ImmutableAddressLabel.builder()
                                        .withName(voter.fullName())
                                        .withPostTown(property.postTown())
                                        .withPostCode(property.postCode());
                                builder.withAddressLine1(property.addressLine1());

                                if (!StringExtras.isNullOrEmpty(property.addressLine2())) {
                                    builder.withAddressLine2(property.addressLine2());
                                }

                                return builder.build();
                            }
                    )
                    .collect(toList());
        }
    }
}
