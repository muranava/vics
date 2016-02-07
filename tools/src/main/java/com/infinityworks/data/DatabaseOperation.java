package com.infinityworks.data;

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
}
