package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.dto.Property;
import com.infinityworks.webapp.clients.paf.dto.PropertyResponse;
import com.infinityworks.webapp.pdf.AddressLabel;
import com.infinityworks.webapp.pdf.ImmutableAddressLabel;
import org.apache.commons.lang3.math.NumberUtils;
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
            String street = property.street().replace(", ", "\n").trim();

            return property.voters().stream()
                    .map(voter -> {
                                ImmutableAddressLabel.Builder builder = ImmutableAddressLabel.builder()
                                        .withName(voter.fullName())
                                        .withPostTown(property.postTown())
                                        .withPostCode(property.postCode());

                                if (NumberUtils.isNumber(property.house())) {
                                    builder.withAddressLine1(new StringJoiner(" ")
                                            .add(property.house())
                                            .add(street)
                                            .toString());
                                    builder.withAddressLine2("");
                                } else {
                                    builder.withAddressLine1(property.house());
                                    builder.withAddressLine2(street);
                                }
                                return builder.build();
                            }
                    )
                    .collect(toList());
        }
    }
}
