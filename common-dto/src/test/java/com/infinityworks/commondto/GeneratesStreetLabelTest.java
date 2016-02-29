package com.infinityworks.commondto;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GeneratesStreetLabelTest {
    @Test
    public void returnsTheStreetLabelAllFieldsPresent() throws Exception {
        GeneratesStreetLabel streetLabel = new GeneratesStreetLabel() {
            @Override
            public String getPostCode() {
                return "CV3 3GU";
            }

            @Override
            public String getDependentLocality() {
                return "Warwickshire";
            }

            @Override
            public String getDependentStreet() {
                return "Brook Close";
            }

            @Override
            public String getMainStreet() {
                return "Main Street";
            }
        };

        assertThat(streetLabel.getStreetLabel(), is("Brook Close, Main Street, Warwickshire, CV3 3GU"));
    }

    @Test
    public void handlesNullAndEmptyStrings() throws Exception {
        GeneratesStreetLabel streetLabel = new GeneratesStreetLabel() {
            @Override
            public String getPostCode() {
                return "CV3 3GU";
            }

            @Override
            public String getDependentLocality() {
                return "";
            }

            @Override
            public String getDependentStreet() {
                return null;
            }

            @Override
            public String getMainStreet() {
                return "Main Street";
            }
        };

        assertThat(streetLabel.getStreetLabel(), is("Main Street, CV3 3GU"));
    }
}