package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VPermission;
import com.mycompany.myapp.repository.VPermissionRepository;
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
 * Integration tests for the {@link VPermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VPermissionResourceIT {

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_ORDER = 1D;
    private static final Double UPDATED_ORDER = 2D;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COMPONENT = "AAAAAAAAAA";
    private static final String UPDATED_COMPONENT = "BBBBBBBBBB";

    private static final String DEFAULT_PERMS = "AAAAAAAAAA";
    private static final String UPDATED_PERMS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/v-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VPermissionRepository vPermissionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVPermissionMockMvc;

    private VPermission vPermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VPermission createEntity(EntityManager em) {
        VPermission vPermission = new VPermission()
            .parentId(DEFAULT_PARENT_ID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .order(DEFAULT_ORDER)
            .url(DEFAULT_URL)
            .component(DEFAULT_COMPONENT)
            .perms(DEFAULT_PERMS);
        return vPermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VPermission createUpdatedEntity(EntityManager em) {
        VPermission vPermission = new VPermission()
            .parentId(UPDATED_PARENT_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .order(UPDATED_ORDER)
            .url(UPDATED_URL)
            .component(UPDATED_COMPONENT)
            .perms(UPDATED_PERMS);
        return vPermission;
    }

    @BeforeEach
    public void initTest() {
        vPermission = createEntity(em);
    }

    @Test
    @Transactional
    void createVPermission() throws Exception {
        int databaseSizeBeforeCreate = vPermissionRepository.findAll().size();
        // Create the VPermission
        restVPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vPermission)))
            .andExpect(status().isCreated());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeCreate + 1);
        VPermission testVPermission = vPermissionList.get(vPermissionList.size() - 1);
        assertThat(testVPermission.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testVPermission.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVPermission.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVPermission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVPermission.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVPermission.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testVPermission.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testVPermission.getComponent()).isEqualTo(DEFAULT_COMPONENT);
        assertThat(testVPermission.getPerms()).isEqualTo(DEFAULT_PERMS);
    }

    @Test
    @Transactional
    void createVPermissionWithExistingId() throws Exception {
        // Create the VPermission with an existing ID
        vPermission.setId(1L);

        int databaseSizeBeforeCreate = vPermissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vPermission)))
            .andExpect(status().isBadRequest());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVPermissions() throws Exception {
        // Initialize the database
        vPermissionRepository.saveAndFlush(vPermission);

        // Get all the vPermissionList
        restVPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.doubleValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].component").value(hasItem(DEFAULT_COMPONENT)))
            .andExpect(jsonPath("$.[*].perms").value(hasItem(DEFAULT_PERMS)));
    }

    @Test
    @Transactional
    void getVPermission() throws Exception {
        // Initialize the database
        vPermissionRepository.saveAndFlush(vPermission);

        // Get the vPermission
        restVPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, vPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vPermission.getId().intValue()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER.doubleValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.component").value(DEFAULT_COMPONENT))
            .andExpect(jsonPath("$.perms").value(DEFAULT_PERMS));
    }

    @Test
    @Transactional
    void getNonExistingVPermission() throws Exception {
        // Get the vPermission
        restVPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVPermission() throws Exception {
        // Initialize the database
        vPermissionRepository.saveAndFlush(vPermission);

        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();

        // Update the vPermission
        VPermission updatedVPermission = vPermissionRepository.findById(vPermission.getId()).get();
        // Disconnect from session so that the updates on updatedVPermission are not directly saved in db
        em.detach(updatedVPermission);
        updatedVPermission
            .parentId(UPDATED_PARENT_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .order(UPDATED_ORDER)
            .url(UPDATED_URL)
            .component(UPDATED_COMPONENT)
            .perms(UPDATED_PERMS);

        restVPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVPermission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVPermission))
            )
            .andExpect(status().isOk());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
        VPermission testVPermission = vPermissionList.get(vPermissionList.size() - 1);
        assertThat(testVPermission.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testVPermission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVPermission.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVPermission.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testVPermission.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVPermission.getComponent()).isEqualTo(UPDATED_COMPONENT);
        assertThat(testVPermission.getPerms()).isEqualTo(UPDATED_PERMS);
    }

    @Test
    @Transactional
    void putNonExistingVPermission() throws Exception {
        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();
        vPermission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vPermission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVPermission() throws Exception {
        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();
        vPermission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVPermission() throws Exception {
        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();
        vPermission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVPermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vPermission)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVPermissionWithPatch() throws Exception {
        // Initialize the database
        vPermissionRepository.saveAndFlush(vPermission);

        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();

        // Update the vPermission using partial update
        VPermission partialUpdatedVPermission = new VPermission();
        partialUpdatedVPermission.setId(vPermission.getId());

        partialUpdatedVPermission.name(UPDATED_NAME).order(UPDATED_ORDER).url(UPDATED_URL).component(UPDATED_COMPONENT);

        restVPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVPermission))
            )
            .andExpect(status().isOk());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
        VPermission testVPermission = vPermissionList.get(vPermissionList.size() - 1);
        assertThat(testVPermission.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testVPermission.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVPermission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVPermission.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVPermission.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testVPermission.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVPermission.getComponent()).isEqualTo(UPDATED_COMPONENT);
        assertThat(testVPermission.getPerms()).isEqualTo(DEFAULT_PERMS);
    }

    @Test
    @Transactional
    void fullUpdateVPermissionWithPatch() throws Exception {
        // Initialize the database
        vPermissionRepository.saveAndFlush(vPermission);

        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();

        // Update the vPermission using partial update
        VPermission partialUpdatedVPermission = new VPermission();
        partialUpdatedVPermission.setId(vPermission.getId());

        partialUpdatedVPermission
            .parentId(UPDATED_PARENT_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .order(UPDATED_ORDER)
            .url(UPDATED_URL)
            .component(UPDATED_COMPONENT)
            .perms(UPDATED_PERMS);

        restVPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVPermission))
            )
            .andExpect(status().isOk());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
        VPermission testVPermission = vPermissionList.get(vPermissionList.size() - 1);
        assertThat(testVPermission.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testVPermission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVPermission.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVPermission.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVPermission.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testVPermission.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVPermission.getComponent()).isEqualTo(UPDATED_COMPONENT);
        assertThat(testVPermission.getPerms()).isEqualTo(UPDATED_PERMS);
    }

    @Test
    @Transactional
    void patchNonExistingVPermission() throws Exception {
        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();
        vPermission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVPermission() throws Exception {
        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();
        vPermission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVPermission() throws Exception {
        int databaseSizeBeforeUpdate = vPermissionRepository.findAll().size();
        vPermission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vPermission))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VPermission in the database
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVPermission() throws Exception {
        // Initialize the database
        vPermissionRepository.saveAndFlush(vPermission);

        int databaseSizeBeforeDelete = vPermissionRepository.findAll().size();

        // Delete the vPermission
        restVPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, vPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VPermission> vPermissionList = vPermissionRepository.findAll();
        assertThat(vPermissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
