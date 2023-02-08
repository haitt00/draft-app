package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VUser;
import com.mycompany.myapp.repository.VUserRepository;
import com.mycompany.myapp.service.VUserService;
import com.mycompany.myapp.service.dto.VUserDTO;
import com.mycompany.myapp.service.mapper.VUserMapper;
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
 * Integration tests for the {@link VUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VUserResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULLNAME = "AAAAAAAAAA";
    private static final String UPDATED_FULLNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_VERIFIED = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_VERIFIED = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/v-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VUserRepository vUserRepository;

    @Mock
    private VUserRepository vUserRepositoryMock;

    @Autowired
    private VUserMapper vUserMapper;

    @Mock
    private VUserService vUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVUserMockMvc;

    private VUser vUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VUser createEntity(EntityManager em) {
        VUser vUser = new VUser()
            .username(DEFAULT_USERNAME)
            .fullname(DEFAULT_FULLNAME)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .emailVerified(DEFAULT_EMAIL_VERIFIED)
            .language(DEFAULT_LANGUAGE)
            .enabled(DEFAULT_ENABLED);
        return vUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VUser createUpdatedEntity(EntityManager em) {
        VUser vUser = new VUser()
            .username(UPDATED_USERNAME)
            .fullname(UPDATED_FULLNAME)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .language(UPDATED_LANGUAGE)
            .enabled(UPDATED_ENABLED);
        return vUser;
    }

    @BeforeEach
    public void initTest() {
        vUser = createEntity(em);
    }

    @Test
    @Transactional
    void createVUser() throws Exception {
        int databaseSizeBeforeCreate = vUserRepository.findAll().size();
        // Create the VUser
        VUserDTO vUserDTO = vUserMapper.toDto(vUser);
        restVUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vUserDTO)))
            .andExpect(status().isCreated());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeCreate + 1);
        VUser testVUser = vUserList.get(vUserList.size() - 1);
        assertThat(testVUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testVUser.getFullname()).isEqualTo(DEFAULT_FULLNAME);
        assertThat(testVUser.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testVUser.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testVUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testVUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVUser.getEmailVerified()).isEqualTo(DEFAULT_EMAIL_VERIFIED);
        assertThat(testVUser.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testVUser.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void createVUserWithExistingId() throws Exception {
        // Create the VUser with an existing ID
        vUser.setId(1L);
        VUserDTO vUserDTO = vUserMapper.toDto(vUser);

        int databaseSizeBeforeCreate = vUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVUsers() throws Exception {
        // Initialize the database
        vUserRepository.saveAndFlush(vUser);

        // Get all the vUserList
        restVUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].fullname").value(hasItem(DEFAULT_FULLNAME)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].emailVerified").value(hasItem(DEFAULT_EMAIL_VERIFIED)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(vUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVUser() throws Exception {
        // Initialize the database
        vUserRepository.saveAndFlush(vUser);

        // Get the vUser
        restVUserMockMvc
            .perform(get(ENTITY_API_URL_ID, vUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.fullname").value(DEFAULT_FULLNAME))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.emailVerified").value(DEFAULT_EMAIL_VERIFIED))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVUser() throws Exception {
        // Get the vUser
        restVUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVUser() throws Exception {
        // Initialize the database
        vUserRepository.saveAndFlush(vUser);

        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();

        // Update the vUser
        VUser updatedVUser = vUserRepository.findById(vUser.getId()).get();
        // Disconnect from session so that the updates on updatedVUser are not directly saved in db
        em.detach(updatedVUser);
        updatedVUser
            .username(UPDATED_USERNAME)
            .fullname(UPDATED_FULLNAME)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .language(UPDATED_LANGUAGE)
            .enabled(UPDATED_ENABLED);
        VUserDTO vUserDTO = vUserMapper.toDto(updatedVUser);

        restVUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
        VUser testVUser = vUserList.get(vUserList.size() - 1);
        assertThat(testVUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testVUser.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testVUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testVUser.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testVUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testVUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVUser.getEmailVerified()).isEqualTo(UPDATED_EMAIL_VERIFIED);
        assertThat(testVUser.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testVUser.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingVUser() throws Exception {
        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();
        vUser.setId(count.incrementAndGet());

        // Create the VUser
        VUserDTO vUserDTO = vUserMapper.toDto(vUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVUser() throws Exception {
        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();
        vUser.setId(count.incrementAndGet());

        // Create the VUser
        VUserDTO vUserDTO = vUserMapper.toDto(vUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVUser() throws Exception {
        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();
        vUser.setId(count.incrementAndGet());

        // Create the VUser
        VUserDTO vUserDTO = vUserMapper.toDto(vUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVUserWithPatch() throws Exception {
        // Initialize the database
        vUserRepository.saveAndFlush(vUser);

        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();

        // Update the vUser using partial update
        VUser partialUpdatedVUser = new VUser();
        partialUpdatedVUser.setId(vUser.getId());

        partialUpdatedVUser
            .username(UPDATED_USERNAME)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .enabled(UPDATED_ENABLED);

        restVUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVUser))
            )
            .andExpect(status().isOk());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
        VUser testVUser = vUserList.get(vUserList.size() - 1);
        assertThat(testVUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testVUser.getFullname()).isEqualTo(DEFAULT_FULLNAME);
        assertThat(testVUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testVUser.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testVUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testVUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVUser.getEmailVerified()).isEqualTo(UPDATED_EMAIL_VERIFIED);
        assertThat(testVUser.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testVUser.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateVUserWithPatch() throws Exception {
        // Initialize the database
        vUserRepository.saveAndFlush(vUser);

        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();

        // Update the vUser using partial update
        VUser partialUpdatedVUser = new VUser();
        partialUpdatedVUser.setId(vUser.getId());

        partialUpdatedVUser
            .username(UPDATED_USERNAME)
            .fullname(UPDATED_FULLNAME)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .language(UPDATED_LANGUAGE)
            .enabled(UPDATED_ENABLED);

        restVUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVUser))
            )
            .andExpect(status().isOk());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
        VUser testVUser = vUserList.get(vUserList.size() - 1);
        assertThat(testVUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testVUser.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testVUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testVUser.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testVUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testVUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVUser.getEmailVerified()).isEqualTo(UPDATED_EMAIL_VERIFIED);
        assertThat(testVUser.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testVUser.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingVUser() throws Exception {
        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();
        vUser.setId(count.incrementAndGet());

        // Create the VUser
        VUserDTO vUserDTO = vUserMapper.toDto(vUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVUser() throws Exception {
        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();
        vUser.setId(count.incrementAndGet());

        // Create the VUser
        VUserDTO vUserDTO = vUserMapper.toDto(vUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVUser() throws Exception {
        int databaseSizeBeforeUpdate = vUserRepository.findAll().size();
        vUser.setId(count.incrementAndGet());

        // Create the VUser
        VUserDTO vUserDTO = vUserMapper.toDto(vUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VUser in the database
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVUser() throws Exception {
        // Initialize the database
        vUserRepository.saveAndFlush(vUser);

        int databaseSizeBeforeDelete = vUserRepository.findAll().size();

        // Delete the vUser
        restVUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, vUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VUser> vUserList = vUserRepository.findAll();
        assertThat(vUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
