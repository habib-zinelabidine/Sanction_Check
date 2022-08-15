package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Pendinglist;
import com.mycompany.myapp.repository.PendinglistRepository;
import com.mycompany.myapp.service.criteria.PendinglistCriteria;
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
 * Integration tests for the {@link PendinglistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PendinglistResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATE = false;
    private static final Boolean UPDATED_STATE = true;

    private static final String ENTITY_API_URL = "/api/pendinglists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PendinglistRepository pendinglistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPendinglistMockMvc;

    private Pendinglist pendinglist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pendinglist createEntity(EntityManager em) {
        Pendinglist pendinglist = new Pendinglist().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME).state(DEFAULT_STATE);
        return pendinglist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pendinglist createUpdatedEntity(EntityManager em) {
        Pendinglist pendinglist = new Pendinglist().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).state(UPDATED_STATE);
        return pendinglist;
    }

    @BeforeEach
    public void initTest() {
        pendinglist = createEntity(em);
    }

    @Test
    @Transactional
    void createPendinglist() throws Exception {
        int databaseSizeBeforeCreate = pendinglistRepository.findAll().size();
        // Create the Pendinglist
        restPendinglistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pendinglist)))
            .andExpect(status().isCreated());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeCreate + 1);
        Pendinglist testPendinglist = pendinglistList.get(pendinglistList.size() - 1);
        assertThat(testPendinglist.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPendinglist.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPendinglist.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void createPendinglistWithExistingId() throws Exception {
        // Create the Pendinglist with an existing ID
        pendinglist.setId(1L);

        int databaseSizeBeforeCreate = pendinglistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPendinglistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pendinglist)))
            .andExpect(status().isBadRequest());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPendinglists() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList
        restPendinglistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pendinglist.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())));
    }

    @Test
    @Transactional
    void getPendinglist() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get the pendinglist
        restPendinglistMockMvc
            .perform(get(ENTITY_API_URL_ID, pendinglist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pendinglist.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.booleanValue()));
    }

    @Test
    @Transactional
    void getPendinglistsByIdFiltering() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        Long id = pendinglist.getId();

        defaultPendinglistShouldBeFound("id.equals=" + id);
        defaultPendinglistShouldNotBeFound("id.notEquals=" + id);

        defaultPendinglistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPendinglistShouldNotBeFound("id.greaterThan=" + id);

        defaultPendinglistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPendinglistShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPendinglistsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where firstName equals to DEFAULT_FIRST_NAME
        defaultPendinglistShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the pendinglistList where firstName equals to UPDATED_FIRST_NAME
        defaultPendinglistShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where firstName not equals to DEFAULT_FIRST_NAME
        defaultPendinglistShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the pendinglistList where firstName not equals to UPDATED_FIRST_NAME
        defaultPendinglistShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultPendinglistShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the pendinglistList where firstName equals to UPDATED_FIRST_NAME
        defaultPendinglistShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where firstName is not null
        defaultPendinglistShouldBeFound("firstName.specified=true");

        // Get all the pendinglistList where firstName is null
        defaultPendinglistShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllPendinglistsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where firstName contains DEFAULT_FIRST_NAME
        defaultPendinglistShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the pendinglistList where firstName contains UPDATED_FIRST_NAME
        defaultPendinglistShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where firstName does not contain DEFAULT_FIRST_NAME
        defaultPendinglistShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the pendinglistList where firstName does not contain UPDATED_FIRST_NAME
        defaultPendinglistShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where lastName equals to DEFAULT_LAST_NAME
        defaultPendinglistShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the pendinglistList where lastName equals to UPDATED_LAST_NAME
        defaultPendinglistShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where lastName not equals to DEFAULT_LAST_NAME
        defaultPendinglistShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the pendinglistList where lastName not equals to UPDATED_LAST_NAME
        defaultPendinglistShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultPendinglistShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the pendinglistList where lastName equals to UPDATED_LAST_NAME
        defaultPendinglistShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where lastName is not null
        defaultPendinglistShouldBeFound("lastName.specified=true");

        // Get all the pendinglistList where lastName is null
        defaultPendinglistShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllPendinglistsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where lastName contains DEFAULT_LAST_NAME
        defaultPendinglistShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the pendinglistList where lastName contains UPDATED_LAST_NAME
        defaultPendinglistShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where lastName does not contain DEFAULT_LAST_NAME
        defaultPendinglistShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the pendinglistList where lastName does not contain UPDATED_LAST_NAME
        defaultPendinglistShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPendinglistsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where state equals to DEFAULT_STATE
        defaultPendinglistShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the pendinglistList where state equals to UPDATED_STATE
        defaultPendinglistShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllPendinglistsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where state not equals to DEFAULT_STATE
        defaultPendinglistShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the pendinglistList where state not equals to UPDATED_STATE
        defaultPendinglistShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllPendinglistsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where state in DEFAULT_STATE or UPDATED_STATE
        defaultPendinglistShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the pendinglistList where state equals to UPDATED_STATE
        defaultPendinglistShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllPendinglistsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        // Get all the pendinglistList where state is not null
        defaultPendinglistShouldBeFound("state.specified=true");

        // Get all the pendinglistList where state is null
        defaultPendinglistShouldNotBeFound("state.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPendinglistShouldBeFound(String filter) throws Exception {
        restPendinglistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pendinglist.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())));

        // Check, that the count call also returns 1
        restPendinglistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPendinglistShouldNotBeFound(String filter) throws Exception {
        restPendinglistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPendinglistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPendinglist() throws Exception {
        // Get the pendinglist
        restPendinglistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPendinglist() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();

        // Update the pendinglist
        Pendinglist updatedPendinglist = pendinglistRepository.findById(pendinglist.getId()).get();
        // Disconnect from session so that the updates on updatedPendinglist are not directly saved in db
        em.detach(updatedPendinglist);
        updatedPendinglist.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).state(UPDATED_STATE);

        restPendinglistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPendinglist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPendinglist))
            )
            .andExpect(status().isOk());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
        Pendinglist testPendinglist = pendinglistList.get(pendinglistList.size() - 1);
        assertThat(testPendinglist.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPendinglist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPendinglist.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void putNonExistingPendinglist() throws Exception {
        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();
        pendinglist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPendinglistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pendinglist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pendinglist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPendinglist() throws Exception {
        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();
        pendinglist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPendinglistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pendinglist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPendinglist() throws Exception {
        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();
        pendinglist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPendinglistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pendinglist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePendinglistWithPatch() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();

        // Update the pendinglist using partial update
        Pendinglist partialUpdatedPendinglist = new Pendinglist();
        partialUpdatedPendinglist.setId(pendinglist.getId());

        partialUpdatedPendinglist.state(UPDATED_STATE);

        restPendinglistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPendinglist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPendinglist))
            )
            .andExpect(status().isOk());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
        Pendinglist testPendinglist = pendinglistList.get(pendinglistList.size() - 1);
        assertThat(testPendinglist.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPendinglist.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPendinglist.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void fullUpdatePendinglistWithPatch() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();

        // Update the pendinglist using partial update
        Pendinglist partialUpdatedPendinglist = new Pendinglist();
        partialUpdatedPendinglist.setId(pendinglist.getId());

        partialUpdatedPendinglist.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).state(UPDATED_STATE);

        restPendinglistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPendinglist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPendinglist))
            )
            .andExpect(status().isOk());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
        Pendinglist testPendinglist = pendinglistList.get(pendinglistList.size() - 1);
        assertThat(testPendinglist.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPendinglist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPendinglist.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingPendinglist() throws Exception {
        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();
        pendinglist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPendinglistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pendinglist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pendinglist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPendinglist() throws Exception {
        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();
        pendinglist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPendinglistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pendinglist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPendinglist() throws Exception {
        int databaseSizeBeforeUpdate = pendinglistRepository.findAll().size();
        pendinglist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPendinglistMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pendinglist))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pendinglist in the database
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePendinglist() throws Exception {
        // Initialize the database
        pendinglistRepository.saveAndFlush(pendinglist);

        int databaseSizeBeforeDelete = pendinglistRepository.findAll().size();

        // Delete the pendinglist
        restPendinglistMockMvc
            .perform(delete(ENTITY_API_URL_ID, pendinglist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pendinglist> pendinglistList = pendinglistRepository.findAll();
        assertThat(pendinglistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
