package com.infinityworks.webapp.pdf;

import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class AddressLabelTest {

    @Test
    public void formatsThePrintedLabel() throws Exception {
        ImmutableAddressLabel label = ImmutableAddressLabel.builder()
                .withName("name")
                .withAddressLine1("line1")
                .withAddressLine2("line2")
                .withPostTown("postTown")
                .withPostCode("postCode")
                .build();

        String formatted = label.printFormat();

        assertThat(formatted, containsString("name"));
        assertThat(formatted, containsString("line1"));
        assertThat(formatted, containsString("line2"));
        assertThat(formatted, containsString("postTown"));
        assertThat(formatted, containsString("postCode"));
    }
}
