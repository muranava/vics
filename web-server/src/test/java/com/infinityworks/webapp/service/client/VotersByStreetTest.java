package com.infinityworks.webapp.service.client;

import com.google.common.io.Resources;
import com.infinityworks.commondto.Property;
import com.infinityworks.commondto.VotersByStreet;
import org.junit.Test;

import static com.infinityworks.testsupport.builder.PropertyBuilder.property;
import static com.infinityworks.webapp.common.Json.objectMapper;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VotersByStreetTest {
    @Test
    public void canDeserialise() throws Exception {
        VotersByStreet[] votersByStreet = objectMapper.readValue(Resources.getResource("paf-voters-multiple-streets.json"), VotersByStreet[].class);

        Property property = votersByStreet[0].getProperties().get(0);
        assertThat(property.getBuildingNumber(), is("1"));
        assertThat(property.getDependentLocality(), is(""));
        assertThat(property.getDependentStreet(), is(""));
        assertThat(property.getMainStreet(), is("Kirby Road"));
        assertThat(property.getPostTown(), is("COVENTRY"));
    }

    @Test
    public void generatesStreetLabel() throws Exception {
        VotersByStreet underTest = new VotersByStreet(asList(
                property().withBuildingNumber("31b").withMainStreet("A").withDependentStreet("C").withPostCode("VB1 9UY").build(),
                property().withBuildingNumber("31a").withMainStreet("A").withDependentStreet("C").withPostCode("VB1 9UY").build()
        ));

        assertThat(underTest.getStreetLabel(), is("C, A, VB1 9UY"));
    }
}
