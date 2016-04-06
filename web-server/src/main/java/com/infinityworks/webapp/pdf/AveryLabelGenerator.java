package com.infinityworks.webapp.pdf;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class AveryLabelGenerator implements AddressLabelGenerator {
    @Override
    public ByteArrayOutputStream generateAddressLabels(List<AddressLabel> addressLabels) {
        return null;
    }
}

