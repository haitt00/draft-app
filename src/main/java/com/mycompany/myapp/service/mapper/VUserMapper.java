package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.VRole;
import com.mycompany.myapp.domain.VUser;
import com.mycompany.myapp.service.dto.VRoleDTO;
import com.mycompany.myapp.service.dto.VUserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VUser} and its DTO {@link VUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface VUserMapper extends EntityMapper<VUserDTO, VUser> {
    @Mapping(target = "vRoles", source = "vRoles", qualifiedByName = "vRoleIdSet")
    VUserDTO toDto(VUser s);

    @Mapping(target = "removeVRole", ignore = true)
    VUser toEntity(VUserDTO vUserDTO);

    @Named("vRoleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VRoleDTO toDtoVRoleId(VRole vRole);

    @Named("vRoleIdSet")
    default Set<VRoleDTO> toDtoVRoleIdSet(Set<VRole> vRole) {
        return vRole.stream().map(this::toDtoVRoleId).collect(Collectors.toSet());
    }
}
