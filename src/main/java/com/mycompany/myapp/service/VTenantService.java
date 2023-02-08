package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VTenantDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.VTenant}.
 */
public interface VTenantService {
    /**
     * Save a vTenant.
     *
     * @param vTenantDTO the entity to save.
     * @return the persisted entity.
     */
    VTenantDTO save(VTenantDTO vTenantDTO);

    /**
     * Updates a vTenant.
     *
     * @param vTenantDTO the entity to update.
     * @return the persisted entity.
     */
    VTenantDTO update(VTenantDTO vTenantDTO);

    /**
     * Partially updates a vTenant.
     *
     * @param vTenantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VTenantDTO> partialUpdate(VTenantDTO vTenantDTO);

    /**
     * Get all the vTenants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VTenantDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vTenant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VTenantDTO> findOne(Long id);

    /**
     * Delete the "id" vTenant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
