package com.infinityworks.data;

import com.infinityworks.webapp.domain.ElectorWithAddress;
import com.infinityworks.webapp.domain.Ward;
import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static java.util.UUID.randomUUID;

public interface DatabaseOperation {

    static Operation insertWards(List<Ward> wards) {
        Insert.Builder columns = insertInto("wards")
                .columns("id", "ward_code", "ward_name", "constituency_code", "constituency_name");

        wards.stream().forEach(w -> columns.values(
                        randomUUID(), w.getWardCode(), w.getWardName(), w.getConstituencyCode(), w.getConstituencyName()));

        return columns.build();
    }

    static Operation insertElectorsWithAddresses(List<ElectorWithAddress> electors) {
        Insert.Builder columns = insertInto("electors_enriched")
                .columns("id", "ward_code", "polling_district", "elector_id", "elector_suffix", "ern", "title", "first_name",
                        "last_name", "initial", "dob", "flag", "modified", "created", "house", "street", "postcode", "upid");

        electors.stream().forEach(e -> columns.values(
                randomUUID(), e.getWardCode(), e.getPollingDistrict(), e.getElectorId(), e.getElectorSuffix(), e.getErn(),
                e.getTitle(), e.getFirstName(), e.getLastName(), e.getInitial(), e.getDob(), e.getFlag(), e.getModified(), e.getCreated(),
                e.getHouse(), e.getStreet(), e.getPostCode(), e.getUpid()));

        return columns.build();
    }
}
