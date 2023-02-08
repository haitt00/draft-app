package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VOrganization;
import com.mycompany.myapp.repository.VOrganizationRepository;
import com.mycompany.myapp.service.VOrganizationService;
import com.mycompany.myapp.service.dto.VOrganizationDTO;
import com.mycompany.myapp.service.mapper.VOrganizationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VOrganization}.
 */
@Service
@Transactional
public class VOrganizationServiceImpl implements VOrganizationService {

    private final Logger log = LoggerFactory.getLogger(VOrganizationServiceImpl.class);

    private final VOrganizationRepository vOrganizationRepository;

    private final VOrganizationMapper vOrganizationMapper;

    public VOrganizationServiceImpl(VOrganizationRepository vOrganizationRepository, VOrganizationMapper vOrganizationMapper) {
        this.vOrganizationRepository = vOrganizationRepository;
        this.vOrganizationMapper = vOrganizationMapper;
    }

    @Override
    public VOrganizationDTO save(VOrganizationDTO vOrganizationDTO) {
        log.debug("Request to save VOrganization : {}", vOrganizationDTO);
        VOrganization vOrganization = vOrganizationMapper.toEntity(vOrganizationDTO);
        vOrganization = vOrganizationRepository.save(vOrganization);
        return vOrganizationMapper.toDto(vOrganization);
    }

    @Override
    public VOrganizationDTO update(VOrganizationDTO vOrganizationDTO) {
        log.debug("Request to update VOrganization : {}", vOrganizationDTO);
        VOrganization vOrganization = vOrganizationMapper.toEntity(vOrganizationDTO);
        vOrganization = vOrganizationRepository.save(vOrganization);
        return vOrganizationMapper.toDto(vOrganization);
    }

    @Override
    public Optional<VOrganizationDTO> partialUpdate(VOrganizationDTO vOrganizationDTO) {
        log.debug("Request to partially update VOrganization : {}", vOrganizationDTO);

        return vOrganizationRepository
            .findById(vOrganizationDTO.getId())
            .map(existingVOrganization -> {
                vOrganizationMapper.partialUpdate(existingVOrganization, vOrganizationDTO);

                return existingVOrganization;
            })
            .map(vOrganizationRepository::save)
            .map(vOrganizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VOrganizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VOrganizations");
        return vOrganizationRepository.findAll(pageable).map(vOrganizationMapper::toDto);
    }

    public Page<VOrganizationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vOrganizationRepository.findAllWithEagerRelationships(pageable).map(vOrganizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VOrganizationDTO> findOne(Long id) {
        log.debug("Request to get VOrganization : {}", id);
        return vOrganizationRepository.findOneWithEagerRelationships(id).map(vOrganizationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VOrganization : {}", id);
        vOrganizationRepository.deleteById(id);
    }
}
