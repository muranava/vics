package com.infinityworks.webapp.converter;

import com.infinityworks.webapp.clients.paf.converter.PafStreetRequestConverter;
import com.infinityworks.webapp.clients.paf.dto.GotvVoterRequest;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import org.junit.Test;

import static com.infinityworks.webapp.testsupport.builder.downstream.ElectorsByStreetsRequestBuilder.electorsByStreets;
import static com.infinityworks.webapp.testsupport.builder.downstream.StreetBuilder.street;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GotpvRequestAdapterTest {
    private GotpvRequestAdapter underTest = new GotpvRequestAdapter(new PafStreetRequestConverter());

    @Test
    public void buildsAGotpvRequest() throws Exception {
        ElectorsByStreetsRequest request = electorsByStreets()
                .withStreets(asList(
                        street().withMainStreet("Highfield Road").build(),
                        street().withMainStreet("Amber Road").build(),
                        street().withMainStreet("Sunny Boulevard").build()
                ))
                .build();

        GotvVoterRequest gotvVoterRequest = underTest.apply(request);

        assertThat(gotvVoterRequest.streets(), hasSize(3));
        assertThat(gotvVoterRequest.filter().flags().deceased(), is(Boolean.FALSE));
        assertThat(gotvVoterRequest.filter().flags().hasPV(), is(Boolean.TRUE));
    }
}
