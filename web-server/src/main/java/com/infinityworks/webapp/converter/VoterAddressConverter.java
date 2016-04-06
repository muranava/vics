package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.dto.PropertyResponse;
import com.infinityworks.webapp.pdf.AddressLabel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class VoterAddressConverter implements Function<PropertyResponse, List<AddressLabel>> {
    @Override
    public List<AddressLabel> apply(PropertyResponse propertyResponse) {
        return null;
    }
}
