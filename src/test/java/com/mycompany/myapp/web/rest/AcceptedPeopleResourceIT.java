package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AcceptedPeople;
import com.mycompany.myapp.repository.AcceptedPeopleRepository;
import com.mycompany.myapp.service.criteria.AcceptedPeopleCriteria;
import com.mycompany.myapp.service.dto.AcceptedPeopleDTO;
import com.mycompany.myapp.service.mapper.AcceptedPeopleMapper;
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
 * Integration tests for the {@link AcceptedPeopleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcceptedPeopleResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/accepted-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcceptedPeopleRepository acceptedPeopleRepository;

    @Autowired
    private AcceptedPeopleMapper acceptedPeopleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcceptedPeopleMockMvc;

    private AcceptedPeople acceptedPeople;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcceptedPeople createEntity(EntityManager em) {
        AcceptedPeople acceptedPeople = new AcceptedPeople()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .status(DEFAULT_STATUS);
        return acceptedPeople;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcceptedPeople createUpdatedEntity(EntityManager em) {
        AcceptedPeople acceptedPeople = new AcceptedPeople()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .status(UPDATED_STATUS);
        return acceptedPeople;
    }

    @BeforeEach
    public void initTest() {
        acceptedPeople = createEntity(em);
    }

    @Test
    @Transactional
    void createAcceptedPeople() throws Exception {
        int databaseSizeBeforeCreate = acceptedPeopleRepository.findAll().size();
        // Create the AcceptedPeople
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(acceptedPeople);
        restAcceptedPeopleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeCreate + 1);
        AcceptedPeople testAcceptedPeople = acceptedPeopleList.get(acceptedPeopleList.size() - 1);
        assertThat(testAcceptedPeople.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAcceptedPeople.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAcceptedPeople.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createAcceptedPeopleWithExistingId() throws Exception {
        // Create the AcceptedPeople with an existing ID
        acceptedPeople.setId(1L);
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(acceptedPeople);

        int databaseSizeBeforeCreate = acceptedPeopleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcceptedPeopleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAcceptedPeople() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList
        restAcceptedPeopleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acceptedPeople.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getAcceptedPeople() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get the acceptedPeople
        restAcceptedPeopleMockMvc
            .perform(get(ENTITY_API_URL_ID, acceptedPeople.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acceptedPeople.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getAcceptedPeopleByIdFiltering() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        Long id = acceptedPeople.getId();

        defaultAcceptedPeopleShouldBeFound("id.equals=" + id);
        defaultAcceptedPeopleShouldNotBeFound("id.notEquals=" + id);

        defaultAcceptedPeopleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAcceptedPeopleShouldNotBeFound("id.greaterThan=" + id);

        defaultAcceptedPeopleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAcceptedPeopleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where firstName equals to DEFAULT_FIRST_NAME
        defaultAcceptedPeopleShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the acceptedPeopleList where firstName equals to UPDATED_FIRST_NAME
        defaultAcceptedPeopleShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where firstName not equals to DEFAULT_FIRST_NAME
        defaultAcceptedPeopleShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the acceptedPeopleList where firstName not equals to UPDATED_FIRST_NAME
        defaultAcceptedPeopleShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultAcceptedPeopleShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the acceptedPeopleList where firstName equals to UPDATED_FIRST_NAME
        defaultAcceptedPeopleShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where firstName is not null
        defaultAcceptedPeopleShouldBeFound("firstName.specified=true");

        // Get all the acceptedPeopleList where firstName is null
        defaultAcceptedPeopleShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where firstName contains DEFAULT_FIRST_NAME
        defaultAcceptedPeopleShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the acceptedPeopleList where firstName contains UPDATED_FIRST_NAME
        defaultAcceptedPeopleShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where firstName does not contain DEFAULT_FIRST_NAME
        defaultAcceptedPeopleShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the acceptedPeopleList where firstName does not contain UPDATED_FIRST_NAME
        defaultAcceptedPeopleShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where lastName equals to DEFAULT_LAST_NAME
        defaultAcceptedPeopleShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the acceptedPeopleList where lastName equals to UPDATED_LAST_NAME
        defaultAcceptedPeopleShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where lastName not equals to DEFAULT_LAST_NAME
        defaultAcceptedPeopleShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the acceptedPeopleList where lastName not equals to UPDATED_LAST_NAME
        defaultAcceptedPeopleShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultAcceptedPeopleShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the acceptedPeopleList where lastName equals to UPDATED_LAST_NAME
        defaultAcceptedPeopleShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where lastName is not null
        defaultAcceptedPeopleShouldBeFound("lastName.specified=true");

        // Get all the acceptedPeopleList where lastName is null
        defaultAcceptedPeopleShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByLastNameContainsSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where lastName contains DEFAULT_LAST_NAME
        defaultAcceptedPeopleShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the acceptedPeopleList where lastName contains UPDATED_LAST_NAME
        defaultAcceptedPeopleShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where lastName does not contain DEFAULT_LAST_NAME
        defaultAcceptedPeopleShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the acceptedPeopleList where lastName does not contain UPDATED_LAST_NAME
        defaultAcceptedPeopleShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where status equals to DEFAULT_STATUS
        defaultAcceptedPeopleShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the acceptedPeopleList where status equals to UPDATED_STATUS
        defaultAcceptedPeopleShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where status not equals to DEFAULT_STATUS
        defaultAcceptedPeopleShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the acceptedPeopleList where status not equals to UPDATED_STATUS
        defaultAcceptedPeopleShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAcceptedPeopleShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the acceptedPeopleList where status equals to UPDATED_STATUS
        defaultAcceptedPeopleShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAcceptedPeopleByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        // Get all the acceptedPeopleList where status is not null
        defaultAcceptedPeopleShouldBeFound("status.specified=true");

        // Get all the acceptedPeopleList where status is null
        defaultAcceptedPeopleShouldNotBeFound("status.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAcceptedPeopleShouldBeFound(String filter) throws Exception {
        restAcceptedPeopleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acceptedPeople.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));

        // Check, that the count call also returns 1
        restAcceptedPeopleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAcceptedPeopleShouldNotBeFound(String filter) throws Exception {
        restAcceptedPeopleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAcceptedPeopleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAcceptedPeople() throws Exception {
        // Get the acceptedPeople
        restAcceptedPeopleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcceptedPeople() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();

        // Update the acceptedPeople
        AcceptedPeople updatedAcceptedPeople = acceptedPeopleRepository.findById(acceptedPeople.getId()).get();
        // Disconnect from session so that the updates on updatedAcceptedPeople are not directly saved in db
        em.detach(updatedAcceptedPeople);
        updatedAcceptedPeople.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).status(UPDATED_STATUS);
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(updatedAcceptedPeople);

        restAcceptedPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acceptedPeopleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isOk());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
        AcceptedPeople testAcceptedPeople = acceptedPeopleList.get(acceptedPeopleList.size() - 1);
        assertThat(testAcceptedPeople.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAcceptedPeople.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAcceptedPeople.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingAcceptedPeople() throws Exception {
        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();
        acceptedPeople.setId(count.incrementAndGet());

        // Create the AcceptedPeople
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(acceptedPeople);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcceptedPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acceptedPeopleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcceptedPeople() throws Exception {
        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();
        acceptedPeople.setId(count.incrementAndGet());

        // Create the AcceptedPeople
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(acceptedPeople);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcceptedPeople() throws Exception {
        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();
        acceptedPeople.setId(count.incrementAndGet());

        // Create the AcceptedPeople
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(acceptedPeople);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedPeopleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcceptedPeopleWithPatch() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();

        // Update the acceptedPeople using partial update
        AcceptedPeople partialUpdatedAcceptedPeople = new AcceptedPeople();
        partialUpdatedAcceptedPeople.setId(acceptedPeople.getId());

        partialUpdatedAcceptedPeople.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restAcceptedPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcceptedPeople.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcceptedPeople))
            )
            .andExpect(status().isOk());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
        AcceptedPeople testAcceptedPeople = acceptedPeopleList.get(acceptedPeopleList.size() - 1);
        assertThat(testAcceptedPeople.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAcceptedPeople.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAcceptedPeople.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateAcceptedPeopleWithPatch() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();

        // Update the acceptedPeople using partial update
        AcceptedPeople partialUpdatedAcceptedPeople = new AcceptedPeople();
        partialUpdatedAcceptedPeople.setId(acceptedPeople.getId());

        partialUpdatedAcceptedPeople.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).status(UPDATED_STATUS);

        restAcceptedPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcceptedPeople.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcceptedPeople))
            )
            .andExpect(status().isOk());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
        AcceptedPeople testAcceptedPeople = acceptedPeopleList.get(acceptedPeopleList.size() - 1);
        assertThat(testAcceptedPeople.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAcceptedPeople.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAcceptedPeople.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingAcceptedPeople() throws Exception {
        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();
        acceptedPeople.setId(count.incrementAndGet());

        // Create the AcceptedPeople
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(acceptedPeople);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcceptedPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acceptedPeopleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcceptedPeople() throws Exception {
        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();
        acceptedPeople.setId(count.incrementAndGet());

        // Create the AcceptedPeople
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(acceptedPeople);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcceptedPeople() throws Exception {
        int databaseSizeBeforeUpdate = acceptedPeopleRepository.findAll().size();
        acceptedPeople.setId(count.incrementAndGet());

        // Create the AcceptedPeople
        AcceptedPeopleDTO acceptedPeopleDTO = acceptedPeopleMapper.toDto(acceptedPeople);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acceptedPeopleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcceptedPeople in the database
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcceptedPeople() throws Exception {
        // Initialize the database
        acceptedPeopleRepository.saveAndFlush(acceptedPeople);

        int databaseSizeBeforeDelete = acceptedPeopleRepository.findAll().size();

        // Delete the acceptedPeople
        restAcceptedPeopleMockMvc
            .perform(delete(ENTITY_API_URL_ID, acceptedPeople.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcceptedPeople> acceptedPeopleList = acceptedPeopleRepository.findAll();
        assertThat(acceptedPeopleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
