package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VAbstractAuditingEntity;
import com.mycompany.myapp.repository.VAbstractAuditingEntityRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link VAbstractAuditingEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VAbstractAuditingEntityResourceIT {

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_MODIFIED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DEL_FLAG = false;
    private static final Boolean UPDATED_DEL_FLAG = true;

    private static final String ENTITY_API_URL = "/api/v-abstract-auditing-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VAbstractAuditingEntityRepository vAbstractAuditingEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVAbstractAuditingEntityMockMvc;

    private VAbstractAuditingEntity vAbstractAuditingEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VAbstractAuditingEntity createEntity(EntityManager em) {
        VAbstractAuditingEntity vAbstractAuditingEntity = new VAbstractAuditingEntity()
            .createdBy(DEFAULT_CREATED_BY)
            .createdTime(DEFAULT_CREATED_TIME)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedTime(DEFAULT_MODIFIED_TIME)
            .delFlag(DEFAULT_DEL_FLAG);
        return vAbstractAuditingEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VAbstractAuditingEntity createUpdatedEntity(EntityManager em) {
        VAbstractAuditingEntity vAbstractAuditingEntity = new VAbstractAuditingEntity()
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedTime(UPDATED_MODIFIED_TIME)
            .delFlag(UPDATED_DEL_FLAG);
        return vAbstractAuditingEntity;
    }

    @BeforeEach
    public void initTest() {
        vAbstractAuditingEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createVAbstractAuditingEntity() throws Exception {
        int databaseSizeBeforeCreate = vAbstractAuditingEntityRepository.findAll().size();
        // Create the VAbstractAuditingEntity
        restVAbstractAuditingEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vAbstractAuditingEntity))
            )
            .andExpect(status().isCreated());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeCreate + 1);
        VAbstractAuditingEntity testVAbstractAuditingEntity = vAbstractAuditingEntityList.get(vAbstractAuditingEntityList.size() - 1);
        assertThat(testVAbstractAuditingEntity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testVAbstractAuditingEntity.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testVAbstractAuditingEntity.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testVAbstractAuditingEntity.getModifiedTime()).isEqualTo(DEFAULT_MODIFIED_TIME);
        assertThat(testVAbstractAuditingEntity.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
    }

    @Test
    @Transactional
    void createVAbstractAuditingEntityWithExistingId() throws Exception {
        // Create the VAbstractAuditingEntity with an existing ID
        vAbstractAuditingEntity.setId(1L);

        int databaseSizeBeforeCreate = vAbstractAuditingEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVAbstractAuditingEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vAbstractAuditingEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVAbstractAuditingEntities() throws Exception {
        // Initialize the database
        vAbstractAuditingEntityRepository.saveAndFlush(vAbstractAuditingEntity);

        // Get all the vAbstractAuditingEntityList
        restVAbstractAuditingEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vAbstractAuditingEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedTime").value(hasItem(DEFAULT_MODIFIED_TIME.toString())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())));
    }

    @Test
    @Transactional
    void getVAbstractAuditingEntity() throws Exception {
        // Initialize the database
        vAbstractAuditingEntityRepository.saveAndFlush(vAbstractAuditingEntity);

        // Get the vAbstractAuditingEntity
        restVAbstractAuditingEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, vAbstractAuditingEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vAbstractAuditingEntity.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedTime").value(DEFAULT_MODIFIED_TIME.toString()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVAbstractAuditingEntity() throws Exception {
        // Get the vAbstractAuditingEntity
        restVAbstractAuditingEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVAbstractAuditingEntity() throws Exception {
        // Initialize the database
        vAbstractAuditingEntityRepository.saveAndFlush(vAbstractAuditingEntity);

        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();

        // Update the vAbstractAuditingEntity
        VAbstractAuditingEntity updatedVAbstractAuditingEntity = vAbstractAuditingEntityRepository
            .findById(vAbstractAuditingEntity.getId())
            .get();
        // Disconnect from session so that the updates on updatedVAbstractAuditingEntity are not directly saved in db
        em.detach(updatedVAbstractAuditingEntity);
        updatedVAbstractAuditingEntity
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedTime(UPDATED_MODIFIED_TIME)
            .delFlag(UPDATED_DEL_FLAG);

        restVAbstractAuditingEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVAbstractAuditingEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVAbstractAuditingEntity))
            )
            .andExpect(status().isOk());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
        VAbstractAuditingEntity testVAbstractAuditingEntity = vAbstractAuditingEntityList.get(vAbstractAuditingEntityList.size() - 1);
        assertThat(testVAbstractAuditingEntity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVAbstractAuditingEntity.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testVAbstractAuditingEntity.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testVAbstractAuditingEntity.getModifiedTime()).isEqualTo(UPDATED_MODIFIED_TIME);
        assertThat(testVAbstractAuditingEntity.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void putNonExistingVAbstractAuditingEntity() throws Exception {
        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();
        vAbstractAuditingEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVAbstractAuditingEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vAbstractAuditingEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vAbstractAuditingEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVAbstractAuditingEntity() throws Exception {
        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();
        vAbstractAuditingEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVAbstractAuditingEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vAbstractAuditingEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVAbstractAuditingEntity() throws Exception {
        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();
        vAbstractAuditingEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVAbstractAuditingEntityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vAbstractAuditingEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVAbstractAuditingEntityWithPatch() throws Exception {
        // Initialize the database
        vAbstractAuditingEntityRepository.saveAndFlush(vAbstractAuditingEntity);

        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();

        // Update the vAbstractAuditingEntity using partial update
        VAbstractAuditingEntity partialUpdatedVAbstractAuditingEntity = new VAbstractAuditingEntity();
        partialUpdatedVAbstractAuditingEntity.setId(vAbstractAuditingEntity.getId());

        partialUpdatedVAbstractAuditingEntity
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .delFlag(UPDATED_DEL_FLAG);

        restVAbstractAuditingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVAbstractAuditingEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVAbstractAuditingEntity))
            )
            .andExpect(status().isOk());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
        VAbstractAuditingEntity testVAbstractAuditingEntity = vAbstractAuditingEntityList.get(vAbstractAuditingEntityList.size() - 1);
        assertThat(testVAbstractAuditingEntity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVAbstractAuditingEntity.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testVAbstractAuditingEntity.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testVAbstractAuditingEntity.getModifiedTime()).isEqualTo(DEFAULT_MODIFIED_TIME);
        assertThat(testVAbstractAuditingEntity.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void fullUpdateVAbstractAuditingEntityWithPatch() throws Exception {
        // Initialize the database
        vAbstractAuditingEntityRepository.saveAndFlush(vAbstractAuditingEntity);

        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();

        // Update the vAbstractAuditingEntity using partial update
        VAbstractAuditingEntity partialUpdatedVAbstractAuditingEntity = new VAbstractAuditingEntity();
        partialUpdatedVAbstractAuditingEntity.setId(vAbstractAuditingEntity.getId());

        partialUpdatedVAbstractAuditingEntity
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedTime(UPDATED_MODIFIED_TIME)
            .delFlag(UPDATED_DEL_FLAG);

        restVAbstractAuditingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVAbstractAuditingEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVAbstractAuditingEntity))
            )
            .andExpect(status().isOk());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
        VAbstractAuditingEntity testVAbstractAuditingEntity = vAbstractAuditingEntityList.get(vAbstractAuditingEntityList.size() - 1);
        assertThat(testVAbstractAuditingEntity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVAbstractAuditingEntity.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testVAbstractAuditingEntity.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testVAbstractAuditingEntity.getModifiedTime()).isEqualTo(UPDATED_MODIFIED_TIME);
        assertThat(testVAbstractAuditingEntity.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void patchNonExistingVAbstractAuditingEntity() throws Exception {
        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();
        vAbstractAuditingEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVAbstractAuditingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vAbstractAuditingEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vAbstractAuditingEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVAbstractAuditingEntity() throws Exception {
        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();
        vAbstractAuditingEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVAbstractAuditingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vAbstractAuditingEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVAbstractAuditingEntity() throws Exception {
        int databaseSizeBeforeUpdate = vAbstractAuditingEntityRepository.findAll().size();
        vAbstractAuditingEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVAbstractAuditingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vAbstractAuditingEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VAbstractAuditingEntity in the database
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVAbstractAuditingEntity() throws Exception {
        // Initialize the database
        vAbstractAuditingEntityRepository.saveAndFlush(vAbstractAuditingEntity);

        int databaseSizeBeforeDelete = vAbstractAuditingEntityRepository.findAll().size();

        // Delete the vAbstractAuditingEntity
        restVAbstractAuditingEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, vAbstractAuditingEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VAbstractAuditingEntity> vAbstractAuditingEntityList = vAbstractAuditingEntityRepository.findAll();
        assertThat(vAbstractAuditingEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
