package com.infinityworks.webapp.converter;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ErnConverterTest {

    @Test
    public void generatesTheErnWithSuffix() throws Exception {
        GeneratesErn generatesErn = new GeneratesErn() {
            @Override
            public String getElectorId() {
                return "electorID";
            }

            @Override
            public String getWardCode() {
                return "wardCode";
            }

            @Override
            public String getElectorSuffix() {
                return "electorSuffix";
            }
        };

        String ern = generatesErn.getErn();

        assertThat(ern, is("wardCode/electorID/electorSuffix"));
    }

    @Test
    public void generatesTheErnWithoutSuffix() throws Exception {
        GeneratesErn generatesErn = new GeneratesErn() {
            @Override
            public String getElectorId() {
                return "electorID";
            }

            @Override
            public String getWardCode() {
                return "wardCode";
            }

            @Override
            public String getElectorSuffix() {
                return null;
            }
        };

        String ern = generatesErn.getErn();

        assertThat(ern, is("wardCode/electorID"));
    }
}