package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VRole;
import com.mycompany.myapp.repository.VRoleRepository;
import com.mycompany.myapp.service.VRoleService;
import com.mycompany.myapp.service.dto.VRoleDTO;
import com.mycompany.myapp.service.mapper.VRoleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VRole}.
 */
@Service
@Transactional
public class VRoleServiceImpl implements VRoleService {

    private final Logger log = LoggerFactory.getLogger(VRoleServiceImpl.class);

    private final VRoleRepository vRoleRepository;

    private final VRoleMapper vRoleMapper;

    public VRoleServiceImpl(VRoleRepository vRoleRepository, VRoleMapper vRoleMapper) {
        this.vRoleRepository = vRoleRepository;
        this.vRoleMapper = vRoleMapper;
    }

    @Override
    public VRoleDTO save(VRoleDTO vRoleDTO) {
        log.debug("Request to save VRole : {}", vRoleDTO);
        VRole vRole = vRoleMapper.toEntity(vRoleDTO);
        vRole = vRoleRepository.save(vRole);
        return vRoleMapper.toDto(vRole);
    }

    @Override
    public VRoleDTO update(VRoleDTO vRoleDTO) {
        log.debug("Request to update VRole : {}", vRoleDTO);
        VRole vRole = vRoleMapper.toEntity(vRoleDTO);
        vRole = vRoleRepository.save(vRole);
        return vRoleMapper.toDto(vRole);
    }

    @Override
    public Optional<VRoleDTO> partialUpdate(VRoleDTO vRoleDTO) {
        log.debug("Request to partially update VRole : {}", vRoleDTO);

        return vRoleRepository
            .findById(vRoleDTO.getId())
            .map(existingVRole -> {
                vRoleMapper.partialUpdate(existingVRole, vRoleDTO);

                return existingVRole;
            })
            .map(vRoleRepository::save)
            .map(vRoleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VRoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VRoles");
        return vRoleRepository.findAll(pageable).map(vRoleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VRoleDTO> findOne(Long id) {
        log.debug("Request to get VRole : {}", id);
        return vRoleRepository.findById(id).map(vRoleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VRole : {}", id);
        vRoleRepository.deleteById(id);
    }
}
