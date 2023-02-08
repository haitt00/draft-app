package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VTenant;
import com.mycompany.myapp.repository.VTenantRepository;
import com.mycompany.myapp.service.VTenantService;
import com.mycompany.myapp.service.dto.VTenantDTO;
import com.mycompany.myapp.service.mapper.VTenantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VTenant}.
 */
@Service
@Transactional
public class VTenantServiceImpl implements VTenantService {

    private final Logger log = LoggerFactory.getLogger(VTenantServiceImpl.class);

    private final VTenantRepository vTenantRepository;

    private final VTenantMapper vTenantMapper;

    public VTenantServiceImpl(VTenantRepository vTenantRepository, VTenantMapper vTenantMapper) {
        this.vTenantRepository = vTenantRepository;
        this.vTenantMapper = vTenantMapper;
    }

    @Override
    public VTenantDTO save(VTenantDTO vTenantDTO) {
        log.debug("Request to save VTenant : {}", vTenantDTO);
        VTenant vTenant = vTenantMapper.toEntity(vTenantDTO);
        vTenant = vTenantRepository.save(vTenant);
        return vTenantMapper.toDto(vTenant);
    }

    @Override
    public VTenantDTO update(VTenantDTO vTenantDTO) {
        log.debug("Request to update VTenant : {}", vTenantDTO);
        VTenant vTenant = vTenantMapper.toEntity(vTenantDTO);
        vTenant = vTenantRepository.save(vTenant);
        return vTenantMapper.toDto(vTenant);
    }

    @Override
    public Optional<VTenantDTO> partialUpdate(VTenantDTO vTenantDTO) {
        log.debug("Request to partially update VTenant : {}", vTenantDTO);

        return vTenantRepository
            .findById(vTenantDTO.getId())
            .map(existingVTenant -> {
                vTenantMapper.partialUpdate(existingVTenant, vTenantDTO);

                return existingVTenant;
            })
            .map(vTenantRepository::save)
            .map(vTenantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VTenantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VTenants");
        return vTenantRepository.findAll(pageable).map(vTenantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VTenantDTO> findOne(Long id) {
        log.debug("Request to get VTenant : {}", id);
        return vTenantRepository.findById(id).map(vTenantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VTenant : {}", id);
        vTenantRepository.deleteById(id);
    }
}
