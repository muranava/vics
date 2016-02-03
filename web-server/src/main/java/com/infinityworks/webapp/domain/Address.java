package com.infinityworks.webapp.domain;

public class Address {
    private final String house;
    private final String street;

    public Address(String house, String street) {
        this.house = house;
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public String getStreet() {
        return street;
    }
}
