package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VOrganizationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.VOrganization}.
 */
public interface VOrganizationService {
    /**
     * Save a vOrganization.
     *
     * @param vOrganizationDTO the entity to save.
     * @return the persisted entity.
     */
    VOrganizationDTO save(VOrganizationDTO vOrganizationDTO);

    /**
     * Updates a vOrganization.
     *
     * @param vOrganizationDTO the entity to update.
     * @return the persisted entity.
     */
    VOrganizationDTO update(VOrganizationDTO vOrganizationDTO);

    /**
     * Partially updates a vOrganization.
     *
     * @param vOrganizationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VOrganizationDTO> partialUpdate(VOrganizationDTO vOrganizationDTO);

    /**
     * Get all the vOrganizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VOrganizationDTO> findAll(Pageable pageable);

    /**
     * Get all the vOrganizations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VOrganizationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vOrganization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VOrganizationDTO> findOne(Long id);

    /**
     * Delete the "id" vOrganization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
