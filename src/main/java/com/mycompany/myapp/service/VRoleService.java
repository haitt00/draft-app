package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VRoleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.VRole}.
 */
public interface VRoleService {
    /**
     * Save a vRole.
     *
     * @param vRoleDTO the entity to save.
     * @return the persisted entity.
     */
    VRoleDTO save(VRoleDTO vRoleDTO);

    /**
     * Updates a vRole.
     *
     * @param vRoleDTO the entity to update.
     * @return the persisted entity.
     */
    VRoleDTO update(VRoleDTO vRoleDTO);

    /**
     * Partially updates a vRole.
     *
     * @param vRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VRoleDTO> partialUpdate(VRoleDTO vRoleDTO);

    /**
     * Get all the vRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VRoleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vRole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VRoleDTO> findOne(Long id);

    /**
     * Delete the "id" vRole.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
