package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.VPermission;
import com.mycompany.myapp.repository.VPermissionRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.VPermission}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VPermissionResource {

    private final Logger log = LoggerFactory.getLogger(VPermissionResource.class);

    private static final String ENTITY_NAME = "vPermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VPermissionRepository vPermissionRepository;

    public VPermissionResource(VPermissionRepository vPermissionRepository) {
        this.vPermissionRepository = vPermissionRepository;
    }

    /**
     * {@code POST  /v-permissions} : Create a new vPermission.
     *
     * @param vPermission the vPermission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vPermission, or with status {@code 400 (Bad Request)} if the vPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-permissions")
    public ResponseEntity<VPermission> createVPermission(@RequestBody VPermission vPermission) throws URISyntaxException {
        log.debug("REST request to save VPermission : {}", vPermission);
        if (vPermission.getId() != null) {
            throw new BadRequestAlertException("A new vPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VPermission result = vPermissionRepository.save(vPermission);
        return ResponseEntity
            .created(new URI("/api/v-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-permissions/:id} : Updates an existing vPermission.
     *
     * @param id the id of the vPermission to save.
     * @param vPermission the vPermission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vPermission,
     * or with status {@code 400 (Bad Request)} if the vPermission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vPermission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-permissions/{id}")
    public ResponseEntity<VPermission> updateVPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VPermission vPermission
    ) throws URISyntaxException {
        log.debug("REST request to update VPermission : {}, {}", id, vPermission);
        if (vPermission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vPermission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VPermission result = vPermissionRepository.save(vPermission);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vPermission.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-permissions/:id} : Partial updates given fields of an existing vPermission, field will ignore if it is null
     *
     * @param id the id of the vPermission to save.
     * @param vPermission the vPermission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vPermission,
     * or with status {@code 400 (Bad Request)} if the vPermission is not valid,
     * or with status {@code 404 (Not Found)} if the vPermission is not found,
     * or with status {@code 500 (Internal Server Error)} if the vPermission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-permissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VPermission> partialUpdateVPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VPermission vPermission
    ) throws URISyntaxException {
        log.debug("REST request to partial update VPermission partially : {}, {}", id, vPermission);
        if (vPermission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vPermission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VPermission> result = vPermissionRepository
            .findById(vPermission.getId())
            .map(existingVPermission -> {
                if (vPermission.getParentId() != null) {
                    existingVPermission.setParentId(vPermission.getParentId());
                }
                if (vPermission.getCode() != null) {
                    existingVPermission.setCode(vPermission.getCode());
                }
                if (vPermission.getName() != null) {
                    existingVPermission.setName(vPermission.getName());
                }
                if (vPermission.getDescription() != null) {
                    existingVPermission.setDescription(vPermission.getDescription());
                }
                if (vPermission.getType() != null) {
                    existingVPermission.setType(vPermission.getType());
                }
                if (vPermission.getOrder() != null) {
                    existingVPermission.setOrder(vPermission.getOrder());
                }
                if (vPermission.getUrl() != null) {
                    existingVPermission.setUrl(vPermission.getUrl());
                }
                if (vPermission.getComponent() != null) {
                    existingVPermission.setComponent(vPermission.getComponent());
                }
                if (vPermission.getPerms() != null) {
                    existingVPermission.setPerms(vPermission.getPerms());
                }

                return existingVPermission;
            })
            .map(vPermissionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vPermission.getId().toString())
        );
    }

    /**
     * {@code GET  /v-permissions} : get all the vPermissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vPermissions in body.
     */
    @GetMapping("/v-permissions")
    public List<VPermission> getAllVPermissions() {
        log.debug("REST request to get all VPermissions");
        return vPermissionRepository.findAll();
    }

    /**
     * {@code GET  /v-permissions/:id} : get the "id" vPermission.
     *
     * @param id the id of the vPermission to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vPermission, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-permissions/{id}")
    public ResponseEntity<VPermission> getVPermission(@PathVariable Long id) {
        log.debug("REST request to get VPermission : {}", id);
        Optional<VPermission> vPermission = vPermissionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vPermission);
    }

    /**
     * {@code DELETE  /v-permissions/:id} : delete the "id" vPermission.
     *
     * @param id the id of the vPermission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-permissions/{id}")
    public ResponseEntity<Void> deleteVPermission(@PathVariable Long id) {
        log.debug("REST request to delete VPermission : {}", id);
        vPermissionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
