package com.infinityworks.webapp.rest.dto;

import com.infinityworks.webapp.domain.Address;
import com.infinityworks.webapp.domain.Elector;
import com.infinityworks.webapp.domain.ElectorWithAddress;

public class ElectorResponse {
    private final String wardCode;
    private final String pollingDistrict;
    private final String electorId;
    private final String electorSuffix;
    private final String title;
    private final String firstName;
    private final String lastName;
    private final String initial;
    private final String house;
    private final String street;

    public ElectorResponse(String wardCode, String pollingDistrict, String electorId, String electorSuffix, String title, String firstName, String lastName, String initial, String house, String street) {
        this.wardCode = wardCode;
        this.pollingDistrict = pollingDistrict;
        this.electorId = electorId;
        this.electorSuffix = electorSuffix;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.initial = initial;
        this.house = house;
        this.street = street;
    }

    public String getWardCode() {
        return wardCode;
    }

    public String getPollingDistrict() {
        return pollingDistrict;
    }

    public String getElectorId() {
        return electorId;
    }

    public String getElectorSuffix() {
        return electorSuffix;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getInitial() {
        return initial;
    }

    public String getHouse() {
        return house;
    }

    public String getStreet() {
        return street;
    }

    public static ElectorResponse of(Elector elector, Address address) {
        return new ElectorResponse(
                elector.getWardCode(),
                elector.getPollingDistrict(),
                elector.getElectorId(),
                elector.getElectorSuffix(),
                elector.getTitle(),
                elector.getFirstName(),
                elector.getLastName(),
                elector.getInitial(),
                address.getHouse(),
                address.getStreet());
    }
}
