package com.infinityworks.webapp.pdf;

import com.infinityworks.common.lang.Try;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface AddressLabelGenerator {
    Try<ByteArrayOutputStream> generateAddressLabels(List<AddressLabel> addressLabels);
}
