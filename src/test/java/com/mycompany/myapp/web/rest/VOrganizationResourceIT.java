package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VOrganization;
import com.mycompany.myapp.repository.VOrganizationRepository;
import com.mycompany.myapp.service.VOrganizationService;
import com.mycompany.myapp.service.dto.VOrganizationDTO;
import com.mycompany.myapp.service.mapper.VOrganizationMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VOrganizationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VOrganizationResourceIT {

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FULL_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final String ENTITY_API_URL = "/api/v-organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VOrganizationRepository vOrganizationRepository;

    @Mock
    private VOrganizationRepository vOrganizationRepositoryMock;

    @Autowired
    private VOrganizationMapper vOrganizationMapper;

    @Mock
    private VOrganizationService vOrganizationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVOrganizationMockMvc;

    private VOrganization vOrganization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VOrganization createEntity(EntityManager em) {
        VOrganization vOrganization = new VOrganization()
            .parentId(DEFAULT_PARENT_ID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .path(DEFAULT_PATH)
            .fullPath(DEFAULT_FULL_PATH)
            .enabled(DEFAULT_ENABLED)
            .type(DEFAULT_TYPE);
        return vOrganization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VOrganization createUpdatedEntity(EntityManager em) {
        VOrganization vOrganization = new VOrganization()
            .parentId(UPDATED_PARENT_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .fullPath(UPDATED_FULL_PATH)
            .enabled(UPDATED_ENABLED)
            .type(UPDATED_TYPE);
        return vOrganization;
    }

    @BeforeEach
    public void initTest() {
        vOrganization = createEntity(em);
    }

    @Test
    @Transactional
    void createVOrganization() throws Exception {
        int databaseSizeBeforeCreate = vOrganizationRepository.findAll().size();
        // Create the VOrganization
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(vOrganization);
        restVOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeCreate + 1);
        VOrganization testVOrganization = vOrganizationList.get(vOrganizationList.size() - 1);
        assertThat(testVOrganization.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testVOrganization.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVOrganization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVOrganization.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testVOrganization.getFullPath()).isEqualTo(DEFAULT_FULL_PATH);
        assertThat(testVOrganization.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testVOrganization.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createVOrganizationWithExistingId() throws Exception {
        // Create the VOrganization with an existing ID
        vOrganization.setId(1L);
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(vOrganization);

        int databaseSizeBeforeCreate = vOrganizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVOrganizations() throws Exception {
        // Initialize the database
        vOrganizationRepository.saveAndFlush(vOrganization);

        // Get all the vOrganizationList
        restVOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].fullPath").value(hasItem(DEFAULT_FULL_PATH)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVOrganizationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vOrganizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVOrganizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vOrganizationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVOrganizationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vOrganizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVOrganizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vOrganizationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVOrganization() throws Exception {
        // Initialize the database
        vOrganizationRepository.saveAndFlush(vOrganization);

        // Get the vOrganization
        restVOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, vOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vOrganization.getId().intValue()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.fullPath").value(DEFAULT_FULL_PATH))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingVOrganization() throws Exception {
        // Get the vOrganization
        restVOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVOrganization() throws Exception {
        // Initialize the database
        vOrganizationRepository.saveAndFlush(vOrganization);

        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();

        // Update the vOrganization
        VOrganization updatedVOrganization = vOrganizationRepository.findById(vOrganization.getId()).get();
        // Disconnect from session so that the updates on updatedVOrganization are not directly saved in db
        em.detach(updatedVOrganization);
        updatedVOrganization
            .parentId(UPDATED_PARENT_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .fullPath(UPDATED_FULL_PATH)
            .enabled(UPDATED_ENABLED)
            .type(UPDATED_TYPE);
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(updatedVOrganization);

        restVOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vOrganizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
        VOrganization testVOrganization = vOrganizationList.get(vOrganizationList.size() - 1);
        assertThat(testVOrganization.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testVOrganization.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVOrganization.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testVOrganization.getFullPath()).isEqualTo(UPDATED_FULL_PATH);
        assertThat(testVOrganization.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testVOrganization.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingVOrganization() throws Exception {
        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();
        vOrganization.setId(count.incrementAndGet());

        // Create the VOrganization
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(vOrganization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vOrganizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVOrganization() throws Exception {
        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();
        vOrganization.setId(count.incrementAndGet());

        // Create the VOrganization
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(vOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVOrganization() throws Exception {
        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();
        vOrganization.setId(count.incrementAndGet());

        // Create the VOrganization
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(vOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVOrganizationWithPatch() throws Exception {
        // Initialize the database
        vOrganizationRepository.saveAndFlush(vOrganization);

        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();

        // Update the vOrganization using partial update
        VOrganization partialUpdatedVOrganization = new VOrganization();
        partialUpdatedVOrganization.setId(vOrganization.getId());

        partialUpdatedVOrganization
            .parentId(UPDATED_PARENT_ID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .type(UPDATED_TYPE);

        restVOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVOrganization))
            )
            .andExpect(status().isOk());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
        VOrganization testVOrganization = vOrganizationList.get(vOrganizationList.size() - 1);
        assertThat(testVOrganization.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testVOrganization.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVOrganization.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testVOrganization.getFullPath()).isEqualTo(DEFAULT_FULL_PATH);
        assertThat(testVOrganization.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testVOrganization.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateVOrganizationWithPatch() throws Exception {
        // Initialize the database
        vOrganizationRepository.saveAndFlush(vOrganization);

        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();

        // Update the vOrganization using partial update
        VOrganization partialUpdatedVOrganization = new VOrganization();
        partialUpdatedVOrganization.setId(vOrganization.getId());

        partialUpdatedVOrganization
            .parentId(UPDATED_PARENT_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .fullPath(UPDATED_FULL_PATH)
            .enabled(UPDATED_ENABLED)
            .type(UPDATED_TYPE);

        restVOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVOrganization))
            )
            .andExpect(status().isOk());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
        VOrganization testVOrganization = vOrganizationList.get(vOrganizationList.size() - 1);
        assertThat(testVOrganization.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testVOrganization.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVOrganization.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testVOrganization.getFullPath()).isEqualTo(UPDATED_FULL_PATH);
        assertThat(testVOrganization.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testVOrganization.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingVOrganization() throws Exception {
        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();
        vOrganization.setId(count.incrementAndGet());

        // Create the VOrganization
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(vOrganization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vOrganizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVOrganization() throws Exception {
        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();
        vOrganization.setId(count.incrementAndGet());

        // Create the VOrganization
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(vOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVOrganization() throws Exception {
        int databaseSizeBeforeUpdate = vOrganizationRepository.findAll().size();
        vOrganization.setId(count.incrementAndGet());

        // Create the VOrganization
        VOrganizationDTO vOrganizationDTO = vOrganizationMapper.toDto(vOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vOrganizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VOrganization in the database
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVOrganization() throws Exception {
        // Initialize the database
        vOrganizationRepository.saveAndFlush(vOrganization);

        int databaseSizeBeforeDelete = vOrganizationRepository.findAll().size();

        // Delete the vOrganization
        restVOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, vOrganization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VOrganization> vOrganizationList = vOrganizationRepository.findAll();
        assertThat(vOrganizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
