package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VUserRepository;
import com.mycompany.myapp.service.VUserService;
import com.mycompany.myapp.service.dto.VUserDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.VUser}.
 */
@RestController
@RequestMapping("/api")
public class VUserResource {

    private final Logger log = LoggerFactory.getLogger(VUserResource.class);

    private static final String ENTITY_NAME = "vUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VUserService vUserService;

    private final VUserRepository vUserRepository;

    public VUserResource(VUserService vUserService, VUserRepository vUserRepository) {
        this.vUserService = vUserService;
        this.vUserRepository = vUserRepository;
    }

    /**
     * {@code POST  /v-users} : Create a new vUser.
     *
     * @param vUserDTO the vUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vUserDTO, or with status {@code 400 (Bad Request)} if the vUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-users")
    public ResponseEntity<VUserDTO> createVUser(@RequestBody VUserDTO vUserDTO) throws URISyntaxException {
        log.debug("REST request to save VUser : {}", vUserDTO);
        if (vUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new vUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VUserDTO result = vUserService.save(vUserDTO);
        return ResponseEntity
            .created(new URI("/api/v-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-users/:id} : Updates an existing vUser.
     *
     * @param id the id of the vUserDTO to save.
     * @param vUserDTO the vUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vUserDTO,
     * or with status {@code 400 (Bad Request)} if the vUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-users/{id}")
    public ResponseEntity<VUserDTO> updateVUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VUserDTO vUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VUser : {}, {}", id, vUserDTO);
        if (vUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VUserDTO result = vUserService.update(vUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-users/:id} : Partial updates given fields of an existing vUser, field will ignore if it is null
     *
     * @param id the id of the vUserDTO to save.
     * @param vUserDTO the vUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vUserDTO,
     * or with status {@code 400 (Bad Request)} if the vUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VUserDTO> partialUpdateVUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VUserDTO vUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VUser partially : {}, {}", id, vUserDTO);
        if (vUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VUserDTO> result = vUserService.partialUpdate(vUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /v-users} : get all the vUsers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vUsers in body.
     */
    @GetMapping("/v-users")
    public ResponseEntity<List<VUserDTO>> getAllVUsers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of VUsers");
        Page<VUserDTO> page;
        if (eagerload) {
            page = vUserService.findAllWithEagerRelationships(pageable);
        } else {
            page = vUserService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /v-users/:id} : get the "id" vUser.
     *
     * @param id the id of the vUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-users/{id}")
    public ResponseEntity<VUserDTO> getVUser(@PathVariable Long id) {
        log.debug("REST request to get VUser : {}", id);
        Optional<VUserDTO> vUserDTO = vUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vUserDTO);
    }

    /**
     * {@code DELETE  /v-users/:id} : delete the "id" vUser.
     *
     * @param id the id of the vUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-users/{id}")
    public ResponseEntity<Void> deleteVUser(@PathVariable Long id) {
        log.debug("REST request to delete VUser : {}", id);
        vUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
