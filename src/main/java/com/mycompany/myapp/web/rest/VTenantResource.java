package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VTenantRepository;
import com.mycompany.myapp.service.VTenantService;
import com.mycompany.myapp.service.dto.VTenantDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.VTenant}.
 */
@RestController
@RequestMapping("/api")
public class VTenantResource {

    private final Logger log = LoggerFactory.getLogger(VTenantResource.class);

    private static final String ENTITY_NAME = "vTenant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VTenantService vTenantService;

    private final VTenantRepository vTenantRepository;

    public VTenantResource(VTenantService vTenantService, VTenantRepository vTenantRepository) {
        this.vTenantService = vTenantService;
        this.vTenantRepository = vTenantRepository;
    }

    /**
     * {@code POST  /v-tenants} : Create a new vTenant.
     *
     * @param vTenantDTO the vTenantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vTenantDTO, or with status {@code 400 (Bad Request)} if the vTenant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-tenants")
    public ResponseEntity<VTenantDTO> createVTenant(@RequestBody VTenantDTO vTenantDTO) throws URISyntaxException {
        log.debug("REST request to save VTenant : {}", vTenantDTO);
        if (vTenantDTO.getId() != null) {
            throw new BadRequestAlertException("A new vTenant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VTenantDTO result = vTenantService.save(vTenantDTO);
        return ResponseEntity
            .created(new URI("/api/v-tenants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-tenants/:id} : Updates an existing vTenant.
     *
     * @param id the id of the vTenantDTO to save.
     * @param vTenantDTO the vTenantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vTenantDTO,
     * or with status {@code 400 (Bad Request)} if the vTenantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vTenantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-tenants/{id}")
    public ResponseEntity<VTenantDTO> updateVTenant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VTenantDTO vTenantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VTenant : {}, {}", id, vTenantDTO);
        if (vTenantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vTenantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vTenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VTenantDTO result = vTenantService.update(vTenantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vTenantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-tenants/:id} : Partial updates given fields of an existing vTenant, field will ignore if it is null
     *
     * @param id the id of the vTenantDTO to save.
     * @param vTenantDTO the vTenantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vTenantDTO,
     * or with status {@code 400 (Bad Request)} if the vTenantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vTenantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vTenantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-tenants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VTenantDTO> partialUpdateVTenant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VTenantDTO vTenantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VTenant partially : {}, {}", id, vTenantDTO);
        if (vTenantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vTenantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vTenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VTenantDTO> result = vTenantService.partialUpdate(vTenantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vTenantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /v-tenants} : get all the vTenants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vTenants in body.
     */
    @GetMapping("/v-tenants")
    public ResponseEntity<List<VTenantDTO>> getAllVTenants(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VTenants");
        Page<VTenantDTO> page = vTenantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /v-tenants/:id} : get the "id" vTenant.
     *
     * @param id the id of the vTenantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vTenantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-tenants/{id}")
    public ResponseEntity<VTenantDTO> getVTenant(@PathVariable Long id) {
        log.debug("REST request to get VTenant : {}", id);
        Optional<VTenantDTO> vTenantDTO = vTenantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vTenantDTO);
    }

    /**
     * {@code DELETE  /v-tenants/:id} : delete the "id" vTenant.
     *
     * @param id the id of the vTenantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-tenants/{id}")
    public ResponseEntity<Void> deleteVTenant(@PathVariable Long id) {
        log.debug("REST request to delete VTenant : {}", id);
        vTenantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
