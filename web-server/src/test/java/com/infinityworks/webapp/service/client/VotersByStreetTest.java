package com.infinityworks.webapp.service.client;

import com.google.common.io.Resources;
import org.junit.Test;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VotersByStreetTest {
    @Test
    public void canDeserialise() throws Exception {
        VotersByStreet[] votersByStreet = objectMapper.readValue(Resources.getResource("json/paf-voters-multiple-streets.json"), VotersByStreet[].class);

        Property property = votersByStreet[0].getProperties().get(0);
        assertThat(property.getBuildingNumber(), is("1"));
        assertThat(property.getDependentLocality(), is(""));
        assertThat(property.getDependentStreet(), is(""));
        assertThat(property.getMainStreet(), is("Kirby Road"));
        assertThat(property.getPostTown(), is("COVENTRY"));
    }
}
