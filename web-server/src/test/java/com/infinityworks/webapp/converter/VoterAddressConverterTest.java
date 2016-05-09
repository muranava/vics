package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.dto.ImmutableProperty;
import com.infinityworks.webapp.clients.paf.dto.ImmutablePropertyResponse;
import com.infinityworks.webapp.clients.paf.dto.ImmutableVoter;
import com.infinityworks.webapp.pdf.AddressLabel;
import com.infinityworks.webapp.pdf.ImmutableAddressLabel;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VoterAddressConverterTest {

    private VoterAddressConverter underTest = new VoterAddressConverter();

    @Test
    public void convertsThePropertiesIntoAddressLabels() throws Exception {
        ImmutablePropertyResponse propertyResponse = propertyResponse();

        List<AddressLabel> addressLabels = underTest.apply(propertyResponse);

        assertThat(addressLabels.size(), is(4));

        assertThat(addressLabels, hasItem(ImmutableAddressLabel.builder()
                .withName("Den Finchley")
                .withAddressLine1("Grange Farmhouse Brandon Lane")
                .withAddressLine2("")
                .withPostTown("Coventry")
                .withPostCode("CV3 3GU")
                .build()));

        assertThat(addressLabels, hasItem(ImmutableAddressLabel.builder()
                .withName("Jan Finchley")
                .withAddressLine1("Grange Farmhouse Brandon Lane")
                .withAddressLine2("")
                .withPostTown("Coventry")
                .withPostCode("CV3 3GU")
                .build()));

        assertThat(addressLabels, hasItem(ImmutableAddressLabel.builder()
                .withName("Alessandro Grasso")
                .withAddressLine1("Grange Bungalow Brandon Lane")
                .withAddressLine2("")
                .withPostTown("Coventry")
                .withPostCode("CV3 3GU")
                .build()));

        assertThat(addressLabels, hasItem(ImmutableAddressLabel.builder()
                .withName("Jeniffer Grasso")
                .withAddressLine1("Grange Bungalow Brandon Lane")
                .withAddressLine2("")
                .withPostTown("Coventry")
                .withPostCode("CV3 3GU")
                .build()));
    }

    private ImmutablePropertyResponse propertyResponse() {
        return ImmutablePropertyResponse.builder()
                .withResponse(singletonList(asList(
                        ImmutableProperty.builder()
                                .withHouse("Grange Farmhouse")
                                .withPostTown("Coventry")
                                .withStreet("Brandon Lane")
                                .withPostCode("CV3 3GU")
                                .withVoters(
                                        asList(
                                                ImmutableVoter.builder()
                                                        .withFullName("Den Finchley")
                                                        .build(),
                                                ImmutableVoter.builder()
                                                        .withFullName("Jan Finchley")
                                                        .build())
                                )
                                .build(),
                        ImmutableProperty.builder()
                                .withHouse("Grange Bungalow")
                                .withPostTown("Coventry")
                                .withStreet("Brandon Lane")
                                .withPostCode("CV3 3GU")
                                .withVoters(
                                        asList(
                                                ImmutableVoter.builder()
                                                        .withFullName("Alessandro Grasso")
                                                        .build(),
                                                ImmutableVoter.builder()
                                                        .withFullName("Jeniffer Grasso")
                                                        .build())
                                )
                                .build()
                )))
                .build();
    }
}
