package com.infinityworks.commondto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GeneratesHouseNumberTest {
    @Test
    public void returnsTheHouseNumberAllFieldsPresent() throws Exception {
        GeneratesHouseNumber generatesHouseNumber = new GeneratesHouseNumber() {
            @Override
            public String getOrganisationName() {
                return "Carpet Warehouse";
            }

            @Override
            public String getBuildingNumber() {
                return "31";
            }

            @Override
            public String getPremise() {
                return "Level 1";
            }

            @Override
            public String getSubBuildingName() {
                return "Flat 31";
            }
        };

        assertThat(generatesHouseNumber.getHouseNumber(), is("31, Flat 31, Level 1, Carpet Warehouse"));
    }

    @Test
    public void returnsOnlyPresentFields() throws Exception {
        GeneratesHouseNumber generatesHouseNumber = new GeneratesHouseNumber() {
            @Override
            public String getOrganisationName() {
                return null;
            }

            @Override
            public String getBuildingNumber() {
                return "2";
            }

            @Override
            public String getPremise() {
                return "";
            }

            @Override
            public String getSubBuildingName() {
                return "Flat 1a";
            }
        };

        assertThat(generatesHouseNumber.getHouseNumber(), is("2, Flat 1a"));
    }
}