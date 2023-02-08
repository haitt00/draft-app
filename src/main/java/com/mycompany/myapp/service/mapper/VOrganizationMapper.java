package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.VOrganization;
import com.mycompany.myapp.domain.VRole;
import com.mycompany.myapp.domain.VUser;
import com.mycompany.myapp.service.dto.VOrganizationDTO;
import com.mycompany.myapp.service.dto.VRoleDTO;
import com.mycompany.myapp.service.dto.VUserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VOrganization} and its DTO {@link VOrganizationDTO}.
 */
@Mapper(componentModel = "spring")
public interface VOrganizationMapper extends EntityMapper<VOrganizationDTO, VOrganization> {
    @Mapping(target = "vUsers", source = "vUsers", qualifiedByName = "vUserIdSet")
    @Mapping(target = "vRoles", source = "vRoles", qualifiedByName = "vRoleIdSet")
    VOrganizationDTO toDto(VOrganization s);

    @Mapping(target = "removeVUser", ignore = true)
    @Mapping(target = "removeVRole", ignore = true)
    VOrganization toEntity(VOrganizationDTO vOrganizationDTO);

    @Named("vUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VUserDTO toDtoVUserId(VUser vUser);

    @Named("vUserIdSet")
    default Set<VUserDTO> toDtoVUserIdSet(Set<VUser> vUser) {
        return vUser.stream().map(this::toDtoVUserId).collect(Collectors.toSet());
    }

    @Named("vRoleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VRoleDTO toDtoVRoleId(VRole vRole);

    @Named("vRoleIdSet")
    default Set<VRoleDTO> toDtoVRoleIdSet(Set<VRole> vRole) {
        return vRole.stream().map(this::toDtoVRoleId).collect(Collectors.toSet());
    }
}
