package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VUser;
import com.mycompany.myapp.repository.VUserRepository;
import com.mycompany.myapp.service.VUserService;
import com.mycompany.myapp.service.dto.VUserDTO;
import com.mycompany.myapp.service.mapper.VUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VUser}.
 */
@Service
@Transactional
public class VUserServiceImpl implements VUserService {

    private final Logger log = LoggerFactory.getLogger(VUserServiceImpl.class);

    private final VUserRepository vUserRepository;

    private final VUserMapper vUserMapper;

    public VUserServiceImpl(VUserRepository vUserRepository, VUserMapper vUserMapper) {
        this.vUserRepository = vUserRepository;
        this.vUserMapper = vUserMapper;
    }

    @Override
    public VUserDTO save(VUserDTO vUserDTO) {
        log.debug("Request to save VUser : {}", vUserDTO);
        VUser vUser = vUserMapper.toEntity(vUserDTO);
        vUser = vUserRepository.save(vUser);
        return vUserMapper.toDto(vUser);
    }

    @Override
    public VUserDTO update(VUserDTO vUserDTO) {
        log.debug("Request to update VUser : {}", vUserDTO);
        VUser vUser = vUserMapper.toEntity(vUserDTO);
        vUser = vUserRepository.save(vUser);
        return vUserMapper.toDto(vUser);
    }

    @Override
    public Optional<VUserDTO> partialUpdate(VUserDTO vUserDTO) {
        log.debug("Request to partially update VUser : {}", vUserDTO);

        return vUserRepository
            .findById(vUserDTO.getId())
            .map(existingVUser -> {
                vUserMapper.partialUpdate(existingVUser, vUserDTO);

                return existingVUser;
            })
            .map(vUserRepository::save)
            .map(vUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VUsers");
        return vUserRepository.findAll(pageable).map(vUserMapper::toDto);
    }

    public Page<VUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vUserRepository.findAllWithEagerRelationships(pageable).map(vUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VUserDTO> findOne(Long id) {
        log.debug("Request to get VUser : {}", id);
        return vUserRepository.findOneWithEagerRelationships(id).map(vUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VUser : {}", id);
        vUserRepository.deleteById(id);
    }
}
