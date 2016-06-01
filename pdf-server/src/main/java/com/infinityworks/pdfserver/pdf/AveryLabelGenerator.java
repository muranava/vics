package com.infinityworks.pdfserver.pdf;

import com.infinityworks.common.lang.Try;
import com.infinityworks.pdfserver.error.PdfGeneratorFailure;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Component
public class AveryLabelGenerator implements AddressLabelGenerator {
    private static final AveryLabelSpec labelSpec = AverySpecs.a4();

    @Override
    public Try<ByteArrayOutputStream> generateAddressLabels(List<AddressLabel> addressLabels) {
        AveryLabelCreator labelGenerator;
        try {
             labelGenerator = new AveryLabelCreator(labelSpec);
        } catch (FileNotFoundException | DocumentException e) {
            return Try.failure(new PdfGeneratorFailure("Failed to create PDF", e));
        }

        for (AddressLabel addressLabel : addressLabels) {
            labelGenerator.createLabel(addressLabel.printFormat());
        }

        return labelGenerator.finish();
    }
}
