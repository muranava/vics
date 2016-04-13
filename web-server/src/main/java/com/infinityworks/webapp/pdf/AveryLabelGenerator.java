package com.infinityworks.webapp.pdf;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.error.PdfGeneratorFailure;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Component
public class AveryLabelGenerator implements AddressLabelGenerator {

    @Override
    public Try<ByteArrayOutputStream> generateAddressLabels(List<AddressLabel> addressLabels) {
        AveryLabelCreator labelGenerator;
        try {
             labelGenerator = new AveryLabelCreator();
        } catch (FileNotFoundException | DocumentException e) {
            return Try.failure(new PdfGeneratorFailure("Failed to create PDF", e));
        }

        for (AddressLabel addressLabel : addressLabels) {
            labelGenerator.createLabel(addressLabel.printFormat());
        }

        return labelGenerator.finish();
    }
}
