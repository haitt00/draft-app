package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.VRole;
import com.mycompany.myapp.service.dto.VRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VRole} and its DTO {@link VRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface VRoleMapper extends EntityMapper<VRoleDTO, VRole> {}
