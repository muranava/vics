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
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public Try<ByteArrayOutputStream> generateAddressLabels(List<AddressLabel> addressLabels) {
        AveryLabelCreator labelGenerator;
        try {
             labelGenerator = new AveryLabelCreator();
        } catch (FileNotFoundException | DocumentException e) {
            return Try.failure(new PdfGeneratorFailure("Failed to create PDF", e));
        }

        String text1 = "Mark Cooper" + LINE_SEPARATOR + "32 Regent Street" + LINE_SEPARATOR + "City of Westminster" + LINE_SEPARATOR + "London" + LINE_SEPARATOR + "KT6 3UB";
        String text2 = "Sean Anderson" + LINE_SEPARATOR + "138 Seven Kings Way" + LINE_SEPARATOR + "Kingston Upon Thames" + LINE_SEPARATOR + "KT6 3UB";

        // TODO remove
        for (int r = 0; r < 300; r++) {
            if (r % 2 == 0) {
                labelGenerator.createLabel(text1);
            } else {
                labelGenerator.createLabel(text2);
            }
        }
        return labelGenerator.finish();
    }
}
