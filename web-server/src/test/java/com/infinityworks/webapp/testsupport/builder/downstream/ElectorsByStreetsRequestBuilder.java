package com.infinityworks.webapp.testsupport.builder.downstream;

import com.infinityworks.webapp.rest.dto.Flags;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.StreetRequest;

import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.downstream.StreetBuilder.street;
import static java.util.Arrays.asList;

public class ElectorsByStreetsRequestBuilder {
    private List<StreetRequest> streets;
    private Flags flags;

    public ElectorsByStreetsRequestBuilder withDefaults() {
        withStreets(asList(
                street().withMainStreet("Highfield Road").build(),
                street().withMainStreet("Amber Road").build(),
                street().withMainStreet("Sunny Boulevard").build()
        ));
        return this;
    }

    public static ElectorsByStreetsRequestBuilder electorsByStreets() {
        return new ElectorsByStreetsRequestBuilder().withDefaults();
    }

    public ElectorsByStreetsRequestBuilder withStreets(List<StreetRequest> streets) {
        this.streets = streets;
        return this;
    }

    public ElectorsByStreetsRequestBuilder withFlags(Flags flags) {
        this.flags = flags;
        return this;
    }

    public ElectorsByStreetsRequest build() {
        return new ElectorsByStreetsRequest(streets, flags);
    }
}