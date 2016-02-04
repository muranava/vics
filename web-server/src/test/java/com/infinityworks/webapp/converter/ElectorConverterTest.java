package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.domain.Address;
import com.infinityworks.webapp.domain.Elector;
import com.infinityworks.webapp.rest.dto.ElectorResponse;
import org.junit.Test;

import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.ElectorBuilder.elector;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ElectorConverterTest {
    private final ElectorConverter underTest = new ElectorConverter();

    @Test
    public void convertsTheAddress() throws Exception {
        Elector adam = elector().withFirstName("adam").build();
        Elector amy = elector().withFirstName("amy")
                .withLastName("gold")
                .withElectorId("eID")
                .withPollingDistrict("PD")
                .withElectorSuffix("eS")
                .withInitial("I")
                .withTitle("Ms")
                .withWardCode("WC")
                .build();
        Address a = new Address("32a", "Malibu Drive");

        List<ElectorResponse> response = underTest.apply(asList(adam, amy), a);

        assertThat(response, hasSize(2));
        ElectorResponse amyMapped = response.get(1);
        assertThat(amyMapped.getElectorId(), is("eID"));
        assertThat(amyMapped.getPollingDistrict(), is("PD"));
        assertThat(amyMapped.getFirstName(), is("amy"));
        assertThat(amyMapped.getHouse(), is("32a"));
        assertThat(amyMapped.getStreet(), is("Malibu Drive"));
        assertThat(amyMapped.getWardCode(), is("WC"));
        assertThat(amyMapped.getTitle(), is("Ms"));


    }
}