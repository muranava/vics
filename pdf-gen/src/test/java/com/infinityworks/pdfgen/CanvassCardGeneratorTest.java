package com.infinityworks.pdfgen;

import com.google.common.io.Resources;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CanvassCardGeneratorTest {
    @Test
    public void generatesThePdf() throws Exception {
        String pafResponse = Resources.toString(Resources.getResource("json/paf-voters-multiple-streets.json"), UTF_8);



    }
}
