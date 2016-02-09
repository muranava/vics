package com.infinityworks.data;

import java.util.UUID;

public class Ward {
    private UUID id;
    private String wardName;
    private String wardCode;
    private UUID constituency;

    public Ward(String wardName, String wardCode, UUID constituency) {
        id = UUID.randomUUID();
        this.wardName = wardName;
        this.wardCode = wardCode;
        this.constituency = constituency;
    }

    public UUID getId() {
        return id;
    }

    public String getWardName() {
        return wardName;
    }

    public String getWardCode() {
        return wardCode;
    }

    public UUID getConstituency() {
        return constituency;
    }
}
