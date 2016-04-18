package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.dto.ImmutablePafStreetResponse;
import com.infinityworks.webapp.rest.dto.Street;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PafToStreetResponseConverterTest {
    @Test
    public void mapsTheStreetResponseFromPaf() {
        PafToStreetResponseConverter underTest = new PafToStreetResponseConverter();

        Street streetResponse = underTest.apply(ImmutablePafStreetResponse.builder()
                .withDependentStreet("dependentStreet")
                .withMainStreet("mainStreet")
                .withCanvassed(10)
                .withVoters(20)
                .withDependentLocality("dependentLocality")
                .withPostTown("postTown")
                .withPriority(2)
                .build()
        );

        assertThat(streetResponse.getMainStreet(), is("mainStreet"));
        assertThat(streetResponse.getDependentStreet(), is("dependentStreet"));
        assertThat(streetResponse.getDependentLocality(), is("dependentLocality"));
        assertThat(streetResponse.getNumCanvassed(), is(10));
        assertThat(streetResponse.getNumVoters(), is(20));
        assertThat(streetResponse.getPostTown(), is("postTown"));
        assertThat(streetResponse.getPriority(), is(2));
    }
}
