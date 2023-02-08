package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.VTenant;
import com.mycompany.myapp.service.dto.VTenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VTenant} and its DTO {@link VTenantDTO}.
 */
@Mapper(componentModel = "spring")
public interface VTenantMapper extends EntityMapper<VTenantDTO, VTenant> {}
