package com.infinityworks.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Set;

/**
 * Note: modifying this class will invalidate all current user sessions,
 * since spring session serializes this entity by default.
 * To change this behaviour we would need to override the spring session methods
 * that persist the session to Redis
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity implements Permissible {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Boolean writeAccess;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_privileges",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privileges_id", referencedColumnName = "id"))
    private Set<Privilege> permissions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_constituencies",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "constituencies_id", referencedColumnName = "id"))
    private Set<Constituency> constituencies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_wards",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "wards_id", referencedColumnName = "id"))
    private Set<Ward> wards;

    @Override
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public boolean hasWardPermission(Ward ward) {
        return isAdmin() || wards.contains(ward) || constituencies.contains(ward.getConstituency());
    }

    @Override
    public boolean hasConstituencyPermission(Constituency constituency) {
        return isAdmin() || constituencies.contains(constituency);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Privilege> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Privilege> permissions) {
        this.permissions = permissions;
    }

    public Set<Constituency> getConstituencies() {
        return constituencies;
    }

    public void setConstituencies(Set<Constituency> constituencies) {
        this.constituencies = constituencies;
    }

    public Set<Ward> getWards() {
        return wards;
    }

    public void setWards(Set<Ward> wards) {
        this.wards = wards;
    }

    public Boolean getWriteAccess() {
        return writeAccess;
    }

    public void setWriteAccess(Boolean writeAccess) {
        this.writeAccess = writeAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equal(username, user.username) &&
               Objects.equal(passwordHash, user.passwordHash) &&
               role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, passwordHash, role);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("username", username)
                .add("role", role)
                .add("permissions", permissions)
                .toString();
    }
}
