package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VTenant;
import com.mycompany.myapp.repository.VTenantRepository;
import com.mycompany.myapp.service.dto.VTenantDTO;
import com.mycompany.myapp.service.mapper.VTenantMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VTenantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VTenantResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/v-tenants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VTenantRepository vTenantRepository;

    @Autowired
    private VTenantMapper vTenantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVTenantMockMvc;

    private VTenant vTenant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VTenant createEntity(EntityManager em) {
        VTenant vTenant = new VTenant().code(DEFAULT_CODE).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).status(DEFAULT_STATUS);
        return vTenant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VTenant createUpdatedEntity(EntityManager em) {
        VTenant vTenant = new VTenant().code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);
        return vTenant;
    }

    @BeforeEach
    public void initTest() {
        vTenant = createEntity(em);
    }

    @Test
    @Transactional
    void createVTenant() throws Exception {
        int databaseSizeBeforeCreate = vTenantRepository.findAll().size();
        // Create the VTenant
        VTenantDTO vTenantDTO = vTenantMapper.toDto(vTenant);
        restVTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vTenantDTO)))
            .andExpect(status().isCreated());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeCreate + 1);
        VTenant testVTenant = vTenantList.get(vTenantList.size() - 1);
        assertThat(testVTenant.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVTenant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVTenant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVTenant.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createVTenantWithExistingId() throws Exception {
        // Create the VTenant with an existing ID
        vTenant.setId(1L);
        VTenantDTO vTenantDTO = vTenantMapper.toDto(vTenant);

        int databaseSizeBeforeCreate = vTenantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVTenantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vTenantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVTenants() throws Exception {
        // Initialize the database
        vTenantRepository.saveAndFlush(vTenant);

        // Get all the vTenantList
        restVTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vTenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getVTenant() throws Exception {
        // Initialize the database
        vTenantRepository.saveAndFlush(vTenant);

        // Get the vTenant
        restVTenantMockMvc
            .perform(get(ENTITY_API_URL_ID, vTenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vTenant.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVTenant() throws Exception {
        // Get the vTenant
        restVTenantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVTenant() throws Exception {
        // Initialize the database
        vTenantRepository.saveAndFlush(vTenant);

        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();

        // Update the vTenant
        VTenant updatedVTenant = vTenantRepository.findById(vTenant.getId()).get();
        // Disconnect from session so that the updates on updatedVTenant are not directly saved in db
        em.detach(updatedVTenant);
        updatedVTenant.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);
        VTenantDTO vTenantDTO = vTenantMapper.toDto(updatedVTenant);

        restVTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vTenantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vTenantDTO))
            )
            .andExpect(status().isOk());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
        VTenant testVTenant = vTenantList.get(vTenantList.size() - 1);
        assertThat(testVTenant.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVTenant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVTenant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVTenant.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingVTenant() throws Exception {
        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();
        vTenant.setId(count.incrementAndGet());

        // Create the VTenant
        VTenantDTO vTenantDTO = vTenantMapper.toDto(vTenant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vTenantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vTenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVTenant() throws Exception {
        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();
        vTenant.setId(count.incrementAndGet());

        // Create the VTenant
        VTenantDTO vTenantDTO = vTenantMapper.toDto(vTenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vTenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVTenant() throws Exception {
        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();
        vTenant.setId(count.incrementAndGet());

        // Create the VTenant
        VTenantDTO vTenantDTO = vTenantMapper.toDto(vTenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVTenantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vTenantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVTenantWithPatch() throws Exception {
        // Initialize the database
        vTenantRepository.saveAndFlush(vTenant);

        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();

        // Update the vTenant using partial update
        VTenant partialUpdatedVTenant = new VTenant();
        partialUpdatedVTenant.setId(vTenant.getId());

        partialUpdatedVTenant.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restVTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVTenant))
            )
            .andExpect(status().isOk());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
        VTenant testVTenant = vTenantList.get(vTenantList.size() - 1);
        assertThat(testVTenant.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVTenant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVTenant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVTenant.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateVTenantWithPatch() throws Exception {
        // Initialize the database
        vTenantRepository.saveAndFlush(vTenant);

        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();

        // Update the vTenant using partial update
        VTenant partialUpdatedVTenant = new VTenant();
        partialUpdatedVTenant.setId(vTenant.getId());

        partialUpdatedVTenant.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restVTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVTenant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVTenant))
            )
            .andExpect(status().isOk());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
        VTenant testVTenant = vTenantList.get(vTenantList.size() - 1);
        assertThat(testVTenant.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVTenant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVTenant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVTenant.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingVTenant() throws Exception {
        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();
        vTenant.setId(count.incrementAndGet());

        // Create the VTenant
        VTenantDTO vTenantDTO = vTenantMapper.toDto(vTenant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vTenantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vTenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVTenant() throws Exception {
        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();
        vTenant.setId(count.incrementAndGet());

        // Create the VTenant
        VTenantDTO vTenantDTO = vTenantMapper.toDto(vTenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vTenantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVTenant() throws Exception {
        int databaseSizeBeforeUpdate = vTenantRepository.findAll().size();
        vTenant.setId(count.incrementAndGet());

        // Create the VTenant
        VTenantDTO vTenantDTO = vTenantMapper.toDto(vTenant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVTenantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vTenantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VTenant in the database
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVTenant() throws Exception {
        // Initialize the database
        vTenantRepository.saveAndFlush(vTenant);

        int databaseSizeBeforeDelete = vTenantRepository.findAll().size();

        // Delete the vTenant
        restVTenantMockMvc
            .perform(delete(ENTITY_API_URL_ID, vTenant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VTenant> vTenantList = vTenantRepository.findAll();
        assertThat(vTenantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
