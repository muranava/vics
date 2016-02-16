package com.infinityworks.webapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinityworks.webapp.domain.Role;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class CreateUserRequest {

    @NotEmpty
    @Email
    private final String email;
    @NotEmpty
    private final String password;
    @NotEmpty
    private final String passwordRepeated;
    @NotNull
    private final Role role;
    @NotNull
    private final List<UUID> wardIDs;
    @NotNull
    private final List<UUID> constituencyIDs;
    @NotNull
    private final Boolean writeAccess;

    @JsonCreator
    public CreateUserRequest(@JsonProperty("email") String email,
                             @JsonProperty("password") String password,
                             @JsonProperty("passwordRepeated") String passwordRepeated,
                             @JsonProperty("role") Role role,
                             @JsonProperty("wardIDs") List<UUID> wardIDs,
                             @JsonProperty("constituencyIDs") List<UUID> constituencyIDs,
                             @JsonProperty("writeAccess") Boolean writeAccess) {
        this.email = email;
        this.password = password;
        this.passwordRepeated = passwordRepeated;
        this.role = role;
        this.wardIDs = wardIDs;
        this.constituencyIDs = constituencyIDs;
        this.writeAccess = writeAccess;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public Role getRole() {
        return role;
    }

    public List<UUID> getWardIDs() {
        return wardIDs;
    }

    public List<UUID> getConstituencyIDs() {
        return constituencyIDs;
    }

    public Boolean getWriteAccess() {
        return writeAccess;
    }
}
