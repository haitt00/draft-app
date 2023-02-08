package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VRoleRepository;
import com.mycompany.myapp.service.VRoleService;
import com.mycompany.myapp.service.dto.VRoleDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.VRole}.
 */
@RestController
@RequestMapping("/api")
public class VRoleResource {

    private final Logger log = LoggerFactory.getLogger(VRoleResource.class);

    private static final String ENTITY_NAME = "vRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VRoleService vRoleService;

    private final VRoleRepository vRoleRepository;

    public VRoleResource(VRoleService vRoleService, VRoleRepository vRoleRepository) {
        this.vRoleService = vRoleService;
        this.vRoleRepository = vRoleRepository;
    }

    /**
     * {@code POST  /v-roles} : Create a new vRole.
     *
     * @param vRoleDTO the vRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vRoleDTO, or with status {@code 400 (Bad Request)} if the vRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-roles")
    public ResponseEntity<VRoleDTO> createVRole(@RequestBody VRoleDTO vRoleDTO) throws URISyntaxException {
        log.debug("REST request to save VRole : {}", vRoleDTO);
        if (vRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new vRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VRoleDTO result = vRoleService.save(vRoleDTO);
        return ResponseEntity
            .created(new URI("/api/v-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-roles/:id} : Updates an existing vRole.
     *
     * @param id the id of the vRoleDTO to save.
     * @param vRoleDTO the vRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vRoleDTO,
     * or with status {@code 400 (Bad Request)} if the vRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-roles/{id}")
    public ResponseEntity<VRoleDTO> updateVRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VRoleDTO vRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VRole : {}, {}", id, vRoleDTO);
        if (vRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VRoleDTO result = vRoleService.update(vRoleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vRoleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-roles/:id} : Partial updates given fields of an existing vRole, field will ignore if it is null
     *
     * @param id the id of the vRoleDTO to save.
     * @param vRoleDTO the vRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vRoleDTO,
     * or with status {@code 400 (Bad Request)} if the vRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VRoleDTO> partialUpdateVRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VRoleDTO vRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VRole partially : {}, {}", id, vRoleDTO);
        if (vRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VRoleDTO> result = vRoleService.partialUpdate(vRoleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vRoleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /v-roles} : get all the vRoles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vRoles in body.
     */
    @GetMapping("/v-roles")
    public ResponseEntity<List<VRoleDTO>> getAllVRoles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VRoles");
        Page<VRoleDTO> page = vRoleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /v-roles/:id} : get the "id" vRole.
     *
     * @param id the id of the vRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-roles/{id}")
    public ResponseEntity<VRoleDTO> getVRole(@PathVariable Long id) {
        log.debug("REST request to get VRole : {}", id);
        Optional<VRoleDTO> vRoleDTO = vRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vRoleDTO);
    }

    /**
     * {@code DELETE  /v-roles/:id} : delete the "id" vRole.
     *
     * @param id the id of the vRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-roles/{id}")
    public ResponseEntity<Void> deleteVRole(@PathVariable Long id) {
        log.debug("REST request to delete VRole : {}", id);
        vRoleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
