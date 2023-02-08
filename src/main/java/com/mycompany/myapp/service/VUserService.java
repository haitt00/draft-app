package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VUserDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.VUser}.
 */
public interface VUserService {
    /**
     * Save a vUser.
     *
     * @param vUserDTO the entity to save.
     * @return the persisted entity.
     */
    VUserDTO save(VUserDTO vUserDTO);

    /**
     * Updates a vUser.
     *
     * @param vUserDTO the entity to update.
     * @return the persisted entity.
     */
    VUserDTO update(VUserDTO vUserDTO);

    /**
     * Partially updates a vUser.
     *
     * @param vUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VUserDTO> partialUpdate(VUserDTO vUserDTO);

    /**
     * Get all the vUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VUserDTO> findAll(Pageable pageable);

    /**
     * Get all the vUsers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VUserDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VUserDTO> findOne(Long id);

    /**
     * Delete the "id" vUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
