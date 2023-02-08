package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VRole;
import com.mycompany.myapp.repository.VRoleRepository;
import com.mycompany.myapp.service.dto.VRoleDTO;
import com.mycompany.myapp.service.mapper.VRoleMapper;
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
 * Integration tests for the {@link VRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VRoleResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/v-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VRoleRepository vRoleRepository;

    @Autowired
    private VRoleMapper vRoleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVRoleMockMvc;

    private VRole vRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VRole createEntity(EntityManager em) {
        VRole vRole = new VRole()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .applicationId(DEFAULT_APPLICATION_ID);
        return vRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VRole createUpdatedEntity(EntityManager em) {
        VRole vRole = new VRole()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .applicationId(UPDATED_APPLICATION_ID);
        return vRole;
    }

    @BeforeEach
    public void initTest() {
        vRole = createEntity(em);
    }

    @Test
    @Transactional
    void createVRole() throws Exception {
        int databaseSizeBeforeCreate = vRoleRepository.findAll().size();
        // Create the VRole
        VRoleDTO vRoleDTO = vRoleMapper.toDto(vRole);
        restVRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vRoleDTO)))
            .andExpect(status().isCreated());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeCreate + 1);
        VRole testVRole = vRoleList.get(vRoleList.size() - 1);
        assertThat(testVRole.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVRole.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
    }

    @Test
    @Transactional
    void createVRoleWithExistingId() throws Exception {
        // Create the VRole with an existing ID
        vRole.setId(1L);
        VRoleDTO vRoleDTO = vRoleMapper.toDto(vRole);

        int databaseSizeBeforeCreate = vRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVRoles() throws Exception {
        // Initialize the database
        vRoleRepository.saveAndFlush(vRole);

        // Get all the vRoleList
        restVRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.intValue())));
    }

    @Test
    @Transactional
    void getVRole() throws Exception {
        // Initialize the database
        vRoleRepository.saveAndFlush(vRole);

        // Get the vRole
        restVRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, vRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vRole.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingVRole() throws Exception {
        // Get the vRole
        restVRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVRole() throws Exception {
        // Initialize the database
        vRoleRepository.saveAndFlush(vRole);

        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();

        // Update the vRole
        VRole updatedVRole = vRoleRepository.findById(vRole.getId()).get();
        // Disconnect from session so that the updates on updatedVRole are not directly saved in db
        em.detach(updatedVRole);
        updatedVRole.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).applicationId(UPDATED_APPLICATION_ID);
        VRoleDTO vRoleDTO = vRoleMapper.toDto(updatedVRole);

        restVRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
        VRole testVRole = vRoleList.get(vRoleList.size() - 1);
        assertThat(testVRole.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVRole.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
    }

    @Test
    @Transactional
    void putNonExistingVRole() throws Exception {
        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();
        vRole.setId(count.incrementAndGet());

        // Create the VRole
        VRoleDTO vRoleDTO = vRoleMapper.toDto(vRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVRole() throws Exception {
        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();
        vRole.setId(count.incrementAndGet());

        // Create the VRole
        VRoleDTO vRoleDTO = vRoleMapper.toDto(vRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVRole() throws Exception {
        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();
        vRole.setId(count.incrementAndGet());

        // Create the VRole
        VRoleDTO vRoleDTO = vRoleMapper.toDto(vRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVRoleWithPatch() throws Exception {
        // Initialize the database
        vRoleRepository.saveAndFlush(vRole);

        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();

        // Update the vRole using partial update
        VRole partialUpdatedVRole = new VRole();
        partialUpdatedVRole.setId(vRole.getId());

        partialUpdatedVRole.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).applicationId(UPDATED_APPLICATION_ID);

        restVRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVRole))
            )
            .andExpect(status().isOk());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
        VRole testVRole = vRoleList.get(vRoleList.size() - 1);
        assertThat(testVRole.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVRole.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
    }

    @Test
    @Transactional
    void fullUpdateVRoleWithPatch() throws Exception {
        // Initialize the database
        vRoleRepository.saveAndFlush(vRole);

        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();

        // Update the vRole using partial update
        VRole partialUpdatedVRole = new VRole();
        partialUpdatedVRole.setId(vRole.getId());

        partialUpdatedVRole.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).applicationId(UPDATED_APPLICATION_ID);

        restVRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVRole))
            )
            .andExpect(status().isOk());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
        VRole testVRole = vRoleList.get(vRoleList.size() - 1);
        assertThat(testVRole.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVRole.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingVRole() throws Exception {
        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();
        vRole.setId(count.incrementAndGet());

        // Create the VRole
        VRoleDTO vRoleDTO = vRoleMapper.toDto(vRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vRoleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVRole() throws Exception {
        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();
        vRole.setId(count.incrementAndGet());

        // Create the VRole
        VRoleDTO vRoleDTO = vRoleMapper.toDto(vRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVRole() throws Exception {
        int databaseSizeBeforeUpdate = vRoleRepository.findAll().size();
        vRole.setId(count.incrementAndGet());

        // Create the VRole
        VRoleDTO vRoleDTO = vRoleMapper.toDto(vRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VRole in the database
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVRole() throws Exception {
        // Initialize the database
        vRoleRepository.saveAndFlush(vRole);

        int databaseSizeBeforeDelete = vRoleRepository.findAll().size();

        // Delete the vRole
        restVRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, vRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VRole> vRoleList = vRoleRepository.findAll();
        assertThat(vRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
