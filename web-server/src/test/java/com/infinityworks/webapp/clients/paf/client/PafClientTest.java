package com.infinityworks.webapp.clients.paf.client;

import com.google.common.io.Resources;
import com.infinityworks.webapp.clients.paf.dto.DeleteContactResponse;
import org.junit.Test;

import static com.infinityworks.webapp.common.Json.objectMapper;
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
}