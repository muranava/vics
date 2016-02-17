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
    private final String username;
    @NotEmpty
    private final String password;
    @NotEmpty
    private final String firstName;
    private final String lastName;
    @NotEmpty
    private final String repeatPassword;
    @NotNull
    private final Role role;
    @NotNull
    private final List<UUID> wardIDs;
    @NotNull
    private final List<UUID> constituencyIDs;
    @NotNull
    private final Boolean writeAccess;

    @JsonCreator
    public CreateUserRequest(@JsonProperty("username") String username,
                             @JsonProperty("password") String password,
                             @JsonProperty("firstName") String firstName,
                             @JsonProperty("lastName") String lastName,
                             @JsonProperty("repeatPassword") String repeatPassword,
                             @JsonProperty("role") Role role,
                             @JsonProperty("wardIDs") List<UUID> wardIDs,
                             @JsonProperty("constituencyIDs") List<UUID> constituencyIDs,
                             @JsonProperty("writeAccess") Boolean writeAccess) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.repeatPassword = repeatPassword;
        this.role = role;
        this.wardIDs = wardIDs;
        this.constituencyIDs = constituencyIDs;
        this.writeAccess = writeAccess;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
