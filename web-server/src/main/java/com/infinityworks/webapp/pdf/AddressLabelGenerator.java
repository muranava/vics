package com.infinityworks.webapp.pdf;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface AddressLabelGenerator {
    ByteArrayOutputStream generateAddressLabels(List<AddressLabel> addressLabels);
}
