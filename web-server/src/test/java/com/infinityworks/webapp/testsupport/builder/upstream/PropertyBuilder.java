package com.infinityworks.webapp.testsupport.builder.upstream;


import com.infinityworks.webapp.paf.dto.Property;
import com.infinityworks.webapp.paf.dto.Voter;

import java.util.Collections;
import java.util.List;

public class PropertyBuilder {
    private String street;
    private String house;
    private String postTown;
    private List<Voter> voters;
    private String postCode;

    public static PropertyBuilder property() {
        return new PropertyBuilder().withDefaults();
    }

    public PropertyBuilder withDefaults() {
        withStreet("Mortimer Crescent")
                .withPostTown("Coventry")
                .withPostCode("CV2 3ER")
                .withHouse("31")
                .withVoters(Collections.emptyList());
        return this;
    }

    public PropertyBuilder withStreet(String street) {
        this.street = street;
        return this;
    }

    public PropertyBuilder withHouse(String house) {
        this.house = house;
        return this;
    }

    public PropertyBuilder withPostTown(String postTown) {
        this.postTown = postTown;
        return this;
    }

    public PropertyBuilder withVoters(List<Voter> voters) {
        this.voters = voters;
        return this;
    }

    public PropertyBuilder withPostCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public Property build() {
        return new Property(street, house, postTown, voters, postCode);
    }
}