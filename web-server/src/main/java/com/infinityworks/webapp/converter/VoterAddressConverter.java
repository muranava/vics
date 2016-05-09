package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.dto.Property;
import com.infinityworks.webapp.clients.paf.dto.PropertyResponse;
import com.infinityworks.webapp.pdf.AddressLabel;
import com.infinityworks.webapp.pdf.ImmutableAddressLabel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
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
                    .map(voter -> ImmutableAddressLabel.builder()
                            .withName(voter.fullName())
                            .withPostTown(property.postTown())
                            .withAddressLine1(new StringJoiner(" ")
                                    .add(property.house())
                                    .add(property.street())
                                    .toString())
                            .withAddressLine2("") // ignored
                            .withPostCode(property.postCode())
                            .build()
                    )
                    .collect(toList());
        }
    }
}
