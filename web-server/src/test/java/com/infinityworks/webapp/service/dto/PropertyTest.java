package com.infinityworks.webapp.service.dto;

import com.google.common.io.Resources;
import com.infinityworks.webapp.paf.dto.Property;
import org.junit.Test;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PropertyTest {
    @Test
    public void deserialisesAProperty() throws Exception {
        Property property = objectMapper.readValue(
                Resources.getResource("json/paf-property.json"), Property.class);

        assertThat(property.getPostCode(), is("NE61 6PG"));
        assertThat(property.getPostTown(), is("Morpeth"));
        assertThat(property.getStreet(), is("Tranwell Court"));
        assertThat(property.getHouse(), is("1"));
    }
}
