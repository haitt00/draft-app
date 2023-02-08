package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.VUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VUserDTO implements Serializable {

    private Long id;

    private String username;

    private String fullname;

    private String firstname;

    private String lastname;

    private String password;

    private String email;

    private String emailVerified;

    private String language;

    private Boolean enabled;

    private Set<VRoleDTO> vRoles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<VRoleDTO> getvRoles() {
        return vRoles;
    }

    public void setvRoles(Set<VRoleDTO> vRoles) {
        this.vRoles = vRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VUserDTO)) {
            return false;
        }

        VUserDTO vUserDTO = (VUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VUserDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", fullname='" + getFullname() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", password='" + getPassword() + "'" +
            ", email='" + getEmail() + "'" +
            ", emailVerified='" + getEmailVerified() + "'" +
            ", language='" + getLanguage() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", vRoles=" + getvRoles() +
            "}";
    }
}
