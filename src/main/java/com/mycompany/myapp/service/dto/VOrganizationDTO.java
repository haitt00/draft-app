package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.VOrganization} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VOrganizationDTO implements Serializable {

    private Long id;

    private Long parentId;

    private String code;

    private String name;

    private String description;

    private String path;

    private String fullPath;

    private Boolean enabled;

    private Integer type;

    private Set<VUserDTO> vUsers = new HashSet<>();

    private Set<VRoleDTO> vRoles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Set<VUserDTO> getvUsers() {
        return vUsers;
    }

    public void setvUsers(Set<VUserDTO> vUsers) {
        this.vUsers = vUsers;
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
        if (!(o instanceof VOrganizationDTO)) {
            return false;
        }

        VOrganizationDTO vOrganizationDTO = (VOrganizationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vOrganizationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VOrganizationDTO{" +
            "id=" + getId() +
            ", parentId=" + getParentId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", path='" + getPath() + "'" +
            ", fullPath='" + getFullPath() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", type=" + getType() +
            ", vUsers=" + getvUsers() +
            ", vRoles=" + getvRoles() +
            "}";
    }
}
