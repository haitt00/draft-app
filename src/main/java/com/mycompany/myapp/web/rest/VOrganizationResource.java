package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VOrganizationRepository;
import com.mycompany.myapp.service.VOrganizationService;
import com.mycompany.myapp.service.dto.VOrganizationDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.VOrganization}.
 */
@RestController
@RequestMapping("/api")
public class VOrganizationResource {

    private final Logger log = LoggerFactory.getLogger(VOrganizationResource.class);

    private static final String ENTITY_NAME = "vOrganization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VOrganizationService vOrganizationService;

    private final VOrganizationRepository vOrganizationRepository;

    public VOrganizationResource(VOrganizationService vOrganizationService, VOrganizationRepository vOrganizationRepository) {
        this.vOrganizationService = vOrganizationService;
        this.vOrganizationRepository = vOrganizationRepository;
    }

    /**
     * {@code POST  /v-organizations} : Create a new vOrganization.
     *
     * @param vOrganizationDTO the vOrganizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vOrganizationDTO, or with status {@code 400 (Bad Request)} if the vOrganization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-organizations")
    public ResponseEntity<VOrganizationDTO> createVOrganization(@RequestBody VOrganizationDTO vOrganizationDTO) throws URISyntaxException {
        log.debug("REST request to save VOrganization : {}", vOrganizationDTO);
        if (vOrganizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new vOrganization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VOrganizationDTO result = vOrganizationService.save(vOrganizationDTO);
        return ResponseEntity
            .created(new URI("/api/v-organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-organizations/:id} : Updates an existing vOrganization.
     *
     * @param id the id of the vOrganizationDTO to save.
     * @param vOrganizationDTO the vOrganizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vOrganizationDTO,
     * or with status {@code 400 (Bad Request)} if the vOrganizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vOrganizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-organizations/{id}")
    public ResponseEntity<VOrganizationDTO> updateVOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VOrganizationDTO vOrganizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VOrganization : {}, {}", id, vOrganizationDTO);
        if (vOrganizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vOrganizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VOrganizationDTO result = vOrganizationService.update(vOrganizationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vOrganizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-organizations/:id} : Partial updates given fields of an existing vOrganization, field will ignore if it is null
     *
     * @param id the id of the vOrganizationDTO to save.
     * @param vOrganizationDTO the vOrganizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vOrganizationDTO,
     * or with status {@code 400 (Bad Request)} if the vOrganizationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vOrganizationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vOrganizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-organizations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VOrganizationDTO> partialUpdateVOrganization(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VOrganizationDTO vOrganizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VOrganization partially : {}, {}", id, vOrganizationDTO);
        if (vOrganizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vOrganizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vOrganizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VOrganizationDTO> result = vOrganizationService.partialUpdate(vOrganizationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vOrganizationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /v-organizations} : get all the vOrganizations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vOrganizations in body.
     */
    @GetMapping("/v-organizations")
    public ResponseEntity<List<VOrganizationDTO>> getAllVOrganizations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of VOrganizations");
        Page<VOrganizationDTO> page;
        if (eagerload) {
            page = vOrganizationService.findAllWithEagerRelationships(pageable);
        } else {
            page = vOrganizationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /v-organizations/:id} : get the "id" vOrganization.
     *
     * @param id the id of the vOrganizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vOrganizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-organizations/{id}")
    public ResponseEntity<VOrganizationDTO> getVOrganization(@PathVariable Long id) {
        log.debug("REST request to get VOrganization : {}", id);
        Optional<VOrganizationDTO> vOrganizationDTO = vOrganizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vOrganizationDTO);
    }

    /**
     * {@code DELETE  /v-organizations/:id} : delete the "id" vOrganization.
     *
     * @param id the id of the vOrganizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-organizations/{id}")
    public ResponseEntity<Void> deleteVOrganization(@PathVariable Long id) {
        log.debug("REST request to delete VOrganization : {}", id);
        vOrganizationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
