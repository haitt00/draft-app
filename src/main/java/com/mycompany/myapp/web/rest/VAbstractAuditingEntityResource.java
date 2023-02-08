package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.VAbstractAuditingEntity;
import com.mycompany.myapp.repository.VAbstractAuditingEntityRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.VAbstractAuditingEntity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VAbstractAuditingEntityResource {

    private final Logger log = LoggerFactory.getLogger(VAbstractAuditingEntityResource.class);

    private static final String ENTITY_NAME = "vAbstractAuditingEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VAbstractAuditingEntityRepository vAbstractAuditingEntityRepository;

    public VAbstractAuditingEntityResource(VAbstractAuditingEntityRepository vAbstractAuditingEntityRepository) {
        this.vAbstractAuditingEntityRepository = vAbstractAuditingEntityRepository;
    }

    /**
     * {@code POST  /v-abstract-auditing-entities} : Create a new vAbstractAuditingEntity.
     *
     * @param vAbstractAuditingEntity the vAbstractAuditingEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vAbstractAuditingEntity, or with status {@code 400 (Bad Request)} if the vAbstractAuditingEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-abstract-auditing-entities")
    public ResponseEntity<VAbstractAuditingEntity> createVAbstractAuditingEntity(
        @RequestBody VAbstractAuditingEntity vAbstractAuditingEntity
    ) throws URISyntaxException {
        log.debug("REST request to save VAbstractAuditingEntity : {}", vAbstractAuditingEntity);
        if (vAbstractAuditingEntity.getId() != null) {
            throw new BadRequestAlertException("A new vAbstractAuditingEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VAbstractAuditingEntity result = vAbstractAuditingEntityRepository.save(vAbstractAuditingEntity);
        return ResponseEntity
            .created(new URI("/api/v-abstract-auditing-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-abstract-auditing-entities/:id} : Updates an existing vAbstractAuditingEntity.
     *
     * @param id the id of the vAbstractAuditingEntity to save.
     * @param vAbstractAuditingEntity the vAbstractAuditingEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vAbstractAuditingEntity,
     * or with status {@code 400 (Bad Request)} if the vAbstractAuditingEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vAbstractAuditingEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-abstract-auditing-entities/{id}")
    public ResponseEntity<VAbstractAuditingEntity> updateVAbstractAuditingEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VAbstractAuditingEntity vAbstractAuditingEntity
    ) throws URISyntaxException {
        log.debug("REST request to update VAbstractAuditingEntity : {}, {}", id, vAbstractAuditingEntity);
        if (vAbstractAuditingEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vAbstractAuditingEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vAbstractAuditingEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VAbstractAuditingEntity result = vAbstractAuditingEntityRepository.save(vAbstractAuditingEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vAbstractAuditingEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-abstract-auditing-entities/:id} : Partial updates given fields of an existing vAbstractAuditingEntity, field will ignore if it is null
     *
     * @param id the id of the vAbstractAuditingEntity to save.
     * @param vAbstractAuditingEntity the vAbstractAuditingEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vAbstractAuditingEntity,
     * or with status {@code 400 (Bad Request)} if the vAbstractAuditingEntity is not valid,
     * or with status {@code 404 (Not Found)} if the vAbstractAuditingEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the vAbstractAuditingEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-abstract-auditing-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VAbstractAuditingEntity> partialUpdateVAbstractAuditingEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VAbstractAuditingEntity vAbstractAuditingEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update VAbstractAuditingEntity partially : {}, {}", id, vAbstractAuditingEntity);
        if (vAbstractAuditingEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vAbstractAuditingEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vAbstractAuditingEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VAbstractAuditingEntity> result = vAbstractAuditingEntityRepository
            .findById(vAbstractAuditingEntity.getId())
            .map(existingVAbstractAuditingEntity -> {
                if (vAbstractAuditingEntity.getCreatedBy() != null) {
                    existingVAbstractAuditingEntity.setCreatedBy(vAbstractAuditingEntity.getCreatedBy());
                }
                if (vAbstractAuditingEntity.getCreatedTime() != null) {
                    existingVAbstractAuditingEntity.setCreatedTime(vAbstractAuditingEntity.getCreatedTime());
                }
                if (vAbstractAuditingEntity.getModifiedBy() != null) {
                    existingVAbstractAuditingEntity.setModifiedBy(vAbstractAuditingEntity.getModifiedBy());
                }
                if (vAbstractAuditingEntity.getModifiedTime() != null) {
                    existingVAbstractAuditingEntity.setModifiedTime(vAbstractAuditingEntity.getModifiedTime());
                }
                if (vAbstractAuditingEntity.getDelFlag() != null) {
                    existingVAbstractAuditingEntity.setDelFlag(vAbstractAuditingEntity.getDelFlag());
                }

                return existingVAbstractAuditingEntity;
            })
            .map(vAbstractAuditingEntityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vAbstractAuditingEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /v-abstract-auditing-entities} : get all the vAbstractAuditingEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vAbstractAuditingEntities in body.
     */
    @GetMapping("/v-abstract-auditing-entities")
    public List<VAbstractAuditingEntity> getAllVAbstractAuditingEntities() {
        log.debug("REST request to get all VAbstractAuditingEntities");
        return vAbstractAuditingEntityRepository.findAll();
    }

    /**
     * {@code GET  /v-abstract-auditing-entities/:id} : get the "id" vAbstractAuditingEntity.
     *
     * @param id the id of the vAbstractAuditingEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vAbstractAuditingEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-abstract-auditing-entities/{id}")
    public ResponseEntity<VAbstractAuditingEntity> getVAbstractAuditingEntity(@PathVariable Long id) {
        log.debug("REST request to get VAbstractAuditingEntity : {}", id);
        Optional<VAbstractAuditingEntity> vAbstractAuditingEntity = vAbstractAuditingEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vAbstractAuditingEntity);
    }

    /**
     * {@code DELETE  /v-abstract-auditing-entities/:id} : delete the "id" vAbstractAuditingEntity.
     *
     * @param id the id of the vAbstractAuditingEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-abstract-auditing-entities/{id}")
    public ResponseEntity<Void> deleteVAbstractAuditingEntity(@PathVariable Long id) {
        log.debug("REST request to delete VAbstractAuditingEntity : {}", id);
        vAbstractAuditingEntityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
