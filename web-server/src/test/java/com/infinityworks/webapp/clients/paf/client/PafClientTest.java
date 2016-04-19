package com.infinityworks.webapp.clients.paf.client;

import com.google.common.io.Resources;
import com.infinityworks.webapp.clients.paf.dto.DeleteContactResponse;
import com.infinityworks.webapp.clients.paf.dto.SearchVoterResponse;
import org.junit.Test;

import static com.infinityworks.webapp.common.JsonUtil.objectMapper;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PafClientTest {
    @Test
    public void deserialisesDeleteContactRequest() throws Exception {
        DeleteContactResponse response = objectMapper.readValue(
                Resources.getResource("json/paf-delete-contact-success.json"), DeleteContactResponse.class);

        assertThat(response.success().message(), is("Contact record deleted"));
        assertThat(response.success().code(), is("GEN-SUCCESS"));
        assertThat(response.success().httpCode(), is(200));
    }

    @Test
    public void deserialisesSearchVoterResponse() throws Exception {
        SearchVoterResponse[] response = objectMapper.readValue(
                Resources.getResource("json/paf-search-voter.json"), SearchVoterResponse[].class);

        SearchVoterResponse first = response[0];
        assertThat(first.surname(), is("Fletcher"));
        assertThat(first.firstName(), is("Paul H R"));
        assertThat(first.fullName(), is("Fletcher, Paul H R"));
        assertThat(first.ern(), is("E05000403-T-1340-0"));
        assertThat(first.address().latitude(), is(51.4139089232));
        assertThat(first.address().longitude(), is(-0.3036031300));
        assertThat(first.address().subBuildingName(), is("Flat 31"));
        assertThat(first.address().buildingName(), is("Bramber House"));
        assertThat(first.address().dependentStreet(), is("Royal Quarter"));
        assertThat(first.address().mainStreet(), is("Seven Kings Way"));
        assertThat(first.address().postTown(), is("KINGSTON UPON THAMES"));
        assertThat(first.address().postcode(), is("KT2 5BU"));
    }
}