package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Sanctionlist;
import com.mycompany.myapp.repository.SanctionlistRepository;
import com.mycompany.myapp.service.criteria.SanctionlistCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SanctionlistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SanctionlistResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DOB = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PASSPORT = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT = "BBBBBBBBBB";

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;
    private static final Integer SMALLER_SCORE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/sanctionlists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SanctionlistRepository sanctionlistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSanctionlistMockMvc;

    private Sanctionlist sanctionlist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sanctionlist createEntity(EntityManager em) {
        Sanctionlist sanctionlist = new Sanctionlist()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .dob(DEFAULT_DOB)
            .address(DEFAULT_ADDRESS)
            .passport(DEFAULT_PASSPORT)
            .score(DEFAULT_SCORE);
        return sanctionlist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sanctionlist createUpdatedEntity(EntityManager em) {
        Sanctionlist sanctionlist = new Sanctionlist()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dob(UPDATED_DOB)
            .address(UPDATED_ADDRESS)
            .passport(UPDATED_PASSPORT)
            .score(UPDATED_SCORE);
        return sanctionlist;
    }

    @BeforeEach
    public void initTest() {
        sanctionlist = createEntity(em);
    }

    @Test
    @Transactional
    void createSanctionlist() throws Exception {
        int databaseSizeBeforeCreate = sanctionlistRepository.findAll().size();
        // Create the Sanctionlist
        restSanctionlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionlist)))
            .andExpect(status().isCreated());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeCreate + 1);
        Sanctionlist testSanctionlist = sanctionlistList.get(sanctionlistList.size() - 1);
        assertThat(testSanctionlist.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSanctionlist.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSanctionlist.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testSanctionlist.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSanctionlist.getPassport()).isEqualTo(DEFAULT_PASSPORT);
        assertThat(testSanctionlist.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    @Transactional
    void createSanctionlistWithExistingId() throws Exception {
        // Create the Sanctionlist with an existing ID
        sanctionlist.setId(1L);

        int databaseSizeBeforeCreate = sanctionlistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSanctionlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionlist)))
            .andExpect(status().isBadRequest());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSanctionlists() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList
        restSanctionlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanctionlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].passport").value(hasItem(DEFAULT_PASSPORT)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)));
    }

    @Test
    @Transactional
    void getSanctionlist() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get the sanctionlist
        restSanctionlistMockMvc
            .perform(get(ENTITY_API_URL_ID, sanctionlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sanctionlist.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.passport").value(DEFAULT_PASSPORT))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE));
    }

    @Test
    @Transactional
    void getSanctionlistsByIdFiltering() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        Long id = sanctionlist.getId();

        defaultSanctionlistShouldBeFound("id.equals=" + id);
        defaultSanctionlistShouldNotBeFound("id.notEquals=" + id);

        defaultSanctionlistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSanctionlistShouldNotBeFound("id.greaterThan=" + id);

        defaultSanctionlistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSanctionlistShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where firstName equals to DEFAULT_FIRST_NAME
        defaultSanctionlistShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the sanctionlistList where firstName equals to UPDATED_FIRST_NAME
        defaultSanctionlistShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where firstName not equals to DEFAULT_FIRST_NAME
        defaultSanctionlistShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the sanctionlistList where firstName not equals to UPDATED_FIRST_NAME
        defaultSanctionlistShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultSanctionlistShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the sanctionlistList where firstName equals to UPDATED_FIRST_NAME
        defaultSanctionlistShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where firstName is not null
        defaultSanctionlistShouldBeFound("firstName.specified=true");

        // Get all the sanctionlistList where firstName is null
        defaultSanctionlistShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionlistsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where firstName contains DEFAULT_FIRST_NAME
        defaultSanctionlistShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the sanctionlistList where firstName contains UPDATED_FIRST_NAME
        defaultSanctionlistShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where firstName does not contain DEFAULT_FIRST_NAME
        defaultSanctionlistShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the sanctionlistList where firstName does not contain UPDATED_FIRST_NAME
        defaultSanctionlistShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where lastName equals to DEFAULT_LAST_NAME
        defaultSanctionlistShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the sanctionlistList where lastName equals to UPDATED_LAST_NAME
        defaultSanctionlistShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where lastName not equals to DEFAULT_LAST_NAME
        defaultSanctionlistShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the sanctionlistList where lastName not equals to UPDATED_LAST_NAME
        defaultSanctionlistShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultSanctionlistShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the sanctionlistList where lastName equals to UPDATED_LAST_NAME
        defaultSanctionlistShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where lastName is not null
        defaultSanctionlistShouldBeFound("lastName.specified=true");

        // Get all the sanctionlistList where lastName is null
        defaultSanctionlistShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionlistsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where lastName contains DEFAULT_LAST_NAME
        defaultSanctionlistShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the sanctionlistList where lastName contains UPDATED_LAST_NAME
        defaultSanctionlistShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where lastName does not contain DEFAULT_LAST_NAME
        defaultSanctionlistShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the sanctionlistList where lastName does not contain UPDATED_LAST_NAME
        defaultSanctionlistShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where dob equals to DEFAULT_DOB
        defaultSanctionlistShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the sanctionlistList where dob equals to UPDATED_DOB
        defaultSanctionlistShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByDobIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where dob not equals to DEFAULT_DOB
        defaultSanctionlistShouldNotBeFound("dob.notEquals=" + DEFAULT_DOB);

        // Get all the sanctionlistList where dob not equals to UPDATED_DOB
        defaultSanctionlistShouldBeFound("dob.notEquals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByDobIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultSanctionlistShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the sanctionlistList where dob equals to UPDATED_DOB
        defaultSanctionlistShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where dob is not null
        defaultSanctionlistShouldBeFound("dob.specified=true");

        // Get all the sanctionlistList where dob is null
        defaultSanctionlistShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionlistsByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where dob is greater than or equal to DEFAULT_DOB
        defaultSanctionlistShouldBeFound("dob.greaterThanOrEqual=" + DEFAULT_DOB);

        // Get all the sanctionlistList where dob is greater than or equal to UPDATED_DOB
        defaultSanctionlistShouldNotBeFound("dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where dob is less than or equal to DEFAULT_DOB
        defaultSanctionlistShouldBeFound("dob.lessThanOrEqual=" + DEFAULT_DOB);

        // Get all the sanctionlistList where dob is less than or equal to SMALLER_DOB
        defaultSanctionlistShouldNotBeFound("dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where dob is less than DEFAULT_DOB
        defaultSanctionlistShouldNotBeFound("dob.lessThan=" + DEFAULT_DOB);

        // Get all the sanctionlistList where dob is less than UPDATED_DOB
        defaultSanctionlistShouldBeFound("dob.lessThan=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where dob is greater than DEFAULT_DOB
        defaultSanctionlistShouldNotBeFound("dob.greaterThan=" + DEFAULT_DOB);

        // Get all the sanctionlistList where dob is greater than SMALLER_DOB
        defaultSanctionlistShouldBeFound("dob.greaterThan=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where address equals to DEFAULT_ADDRESS
        defaultSanctionlistShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the sanctionlistList where address equals to UPDATED_ADDRESS
        defaultSanctionlistShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where address not equals to DEFAULT_ADDRESS
        defaultSanctionlistShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the sanctionlistList where address not equals to UPDATED_ADDRESS
        defaultSanctionlistShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSanctionlistShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the sanctionlistList where address equals to UPDATED_ADDRESS
        defaultSanctionlistShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where address is not null
        defaultSanctionlistShouldBeFound("address.specified=true");

        // Get all the sanctionlistList where address is null
        defaultSanctionlistShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionlistsByAddressContainsSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where address contains DEFAULT_ADDRESS
        defaultSanctionlistShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the sanctionlistList where address contains UPDATED_ADDRESS
        defaultSanctionlistShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where address does not contain DEFAULT_ADDRESS
        defaultSanctionlistShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the sanctionlistList where address does not contain UPDATED_ADDRESS
        defaultSanctionlistShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByPassportIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where passport equals to DEFAULT_PASSPORT
        defaultSanctionlistShouldBeFound("passport.equals=" + DEFAULT_PASSPORT);

        // Get all the sanctionlistList where passport equals to UPDATED_PASSPORT
        defaultSanctionlistShouldNotBeFound("passport.equals=" + UPDATED_PASSPORT);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByPassportIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where passport not equals to DEFAULT_PASSPORT
        defaultSanctionlistShouldNotBeFound("passport.notEquals=" + DEFAULT_PASSPORT);

        // Get all the sanctionlistList where passport not equals to UPDATED_PASSPORT
        defaultSanctionlistShouldBeFound("passport.notEquals=" + UPDATED_PASSPORT);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByPassportIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where passport in DEFAULT_PASSPORT or UPDATED_PASSPORT
        defaultSanctionlistShouldBeFound("passport.in=" + DEFAULT_PASSPORT + "," + UPDATED_PASSPORT);

        // Get all the sanctionlistList where passport equals to UPDATED_PASSPORT
        defaultSanctionlistShouldNotBeFound("passport.in=" + UPDATED_PASSPORT);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByPassportIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where passport is not null
        defaultSanctionlistShouldBeFound("passport.specified=true");

        // Get all the sanctionlistList where passport is null
        defaultSanctionlistShouldNotBeFound("passport.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionlistsByPassportContainsSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where passport contains DEFAULT_PASSPORT
        defaultSanctionlistShouldBeFound("passport.contains=" + DEFAULT_PASSPORT);

        // Get all the sanctionlistList where passport contains UPDATED_PASSPORT
        defaultSanctionlistShouldNotBeFound("passport.contains=" + UPDATED_PASSPORT);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByPassportNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where passport does not contain DEFAULT_PASSPORT
        defaultSanctionlistShouldNotBeFound("passport.doesNotContain=" + DEFAULT_PASSPORT);

        // Get all the sanctionlistList where passport does not contain UPDATED_PASSPORT
        defaultSanctionlistShouldBeFound("passport.doesNotContain=" + UPDATED_PASSPORT);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where score equals to DEFAULT_SCORE
        defaultSanctionlistShouldBeFound("score.equals=" + DEFAULT_SCORE);

        // Get all the sanctionlistList where score equals to UPDATED_SCORE
        defaultSanctionlistShouldNotBeFound("score.equals=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByScoreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where score not equals to DEFAULT_SCORE
        defaultSanctionlistShouldNotBeFound("score.notEquals=" + DEFAULT_SCORE);

        // Get all the sanctionlistList where score not equals to UPDATED_SCORE
        defaultSanctionlistShouldBeFound("score.notEquals=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByScoreIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where score in DEFAULT_SCORE or UPDATED_SCORE
        defaultSanctionlistShouldBeFound("score.in=" + DEFAULT_SCORE + "," + UPDATED_SCORE);

        // Get all the sanctionlistList where score equals to UPDATED_SCORE
        defaultSanctionlistShouldNotBeFound("score.in=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where score is not null
        defaultSanctionlistShouldBeFound("score.specified=true");

        // Get all the sanctionlistList where score is null
        defaultSanctionlistShouldNotBeFound("score.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionlistsByScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where score is greater than or equal to DEFAULT_SCORE
        defaultSanctionlistShouldBeFound("score.greaterThanOrEqual=" + DEFAULT_SCORE);

        // Get all the sanctionlistList where score is greater than or equal to UPDATED_SCORE
        defaultSanctionlistShouldNotBeFound("score.greaterThanOrEqual=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where score is less than or equal to DEFAULT_SCORE
        defaultSanctionlistShouldBeFound("score.lessThanOrEqual=" + DEFAULT_SCORE);

        // Get all the sanctionlistList where score is less than or equal to SMALLER_SCORE
        defaultSanctionlistShouldNotBeFound("score.lessThanOrEqual=" + SMALLER_SCORE);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where score is less than DEFAULT_SCORE
        defaultSanctionlistShouldNotBeFound("score.lessThan=" + DEFAULT_SCORE);

        // Get all the sanctionlistList where score is less than UPDATED_SCORE
        defaultSanctionlistShouldBeFound("score.lessThan=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    void getAllSanctionlistsByScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        // Get all the sanctionlistList where score is greater than DEFAULT_SCORE
        defaultSanctionlistShouldNotBeFound("score.greaterThan=" + DEFAULT_SCORE);

        // Get all the sanctionlistList where score is greater than SMALLER_SCORE
        defaultSanctionlistShouldBeFound("score.greaterThan=" + SMALLER_SCORE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSanctionlistShouldBeFound(String filter) throws Exception {
        restSanctionlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanctionlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].passport").value(hasItem(DEFAULT_PASSPORT)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)));

        // Check, that the count call also returns 1
        restSanctionlistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSanctionlistShouldNotBeFound(String filter) throws Exception {
        restSanctionlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSanctionlistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSanctionlist() throws Exception {
        // Get the sanctionlist
        restSanctionlistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSanctionlist() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();

        // Update the sanctionlist
        Sanctionlist updatedSanctionlist = sanctionlistRepository.findById(sanctionlist.getId()).get();
        // Disconnect from session so that the updates on updatedSanctionlist are not directly saved in db
        em.detach(updatedSanctionlist);
        updatedSanctionlist
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dob(UPDATED_DOB)
            .address(UPDATED_ADDRESS)
            .passport(UPDATED_PASSPORT)
            .score(UPDATED_SCORE);

        restSanctionlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSanctionlist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSanctionlist))
            )
            .andExpect(status().isOk());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
        Sanctionlist testSanctionlist = sanctionlistList.get(sanctionlistList.size() - 1);
        assertThat(testSanctionlist.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSanctionlist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSanctionlist.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testSanctionlist.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSanctionlist.getPassport()).isEqualTo(UPDATED_PASSPORT);
        assertThat(testSanctionlist.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void putNonExistingSanctionlist() throws Exception {
        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();
        sanctionlist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanctionlist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSanctionlist() throws Exception {
        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();
        sanctionlist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSanctionlist() throws Exception {
        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();
        sanctionlist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionlistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionlist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSanctionlistWithPatch() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();

        // Update the sanctionlist using partial update
        Sanctionlist partialUpdatedSanctionlist = new Sanctionlist();
        partialUpdatedSanctionlist.setId(sanctionlist.getId());

        partialUpdatedSanctionlist
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dob(UPDATED_DOB)
            .address(UPDATED_ADDRESS)
            .passport(UPDATED_PASSPORT)
            .score(UPDATED_SCORE);

        restSanctionlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanctionlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanctionlist))
            )
            .andExpect(status().isOk());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
        Sanctionlist testSanctionlist = sanctionlistList.get(sanctionlistList.size() - 1);
        assertThat(testSanctionlist.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSanctionlist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSanctionlist.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testSanctionlist.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSanctionlist.getPassport()).isEqualTo(UPDATED_PASSPORT);
        assertThat(testSanctionlist.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void fullUpdateSanctionlistWithPatch() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();

        // Update the sanctionlist using partial update
        Sanctionlist partialUpdatedSanctionlist = new Sanctionlist();
        partialUpdatedSanctionlist.setId(sanctionlist.getId());

        partialUpdatedSanctionlist
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dob(UPDATED_DOB)
            .address(UPDATED_ADDRESS)
            .passport(UPDATED_PASSPORT)
            .score(UPDATED_SCORE);

        restSanctionlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanctionlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanctionlist))
            )
            .andExpect(status().isOk());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
        Sanctionlist testSanctionlist = sanctionlistList.get(sanctionlistList.size() - 1);
        assertThat(testSanctionlist.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSanctionlist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSanctionlist.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testSanctionlist.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSanctionlist.getPassport()).isEqualTo(UPDATED_PASSPORT);
        assertThat(testSanctionlist.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void patchNonExistingSanctionlist() throws Exception {
        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();
        sanctionlist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sanctionlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSanctionlist() throws Exception {
        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();
        sanctionlist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSanctionlist() throws Exception {
        int databaseSizeBeforeUpdate = sanctionlistRepository.findAll().size();
        sanctionlist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionlistMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sanctionlist))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sanctionlist in the database
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSanctionlist() throws Exception {
        // Initialize the database
        sanctionlistRepository.saveAndFlush(sanctionlist);

        int databaseSizeBeforeDelete = sanctionlistRepository.findAll().size();

        // Delete the sanctionlist
        restSanctionlistMockMvc
            .perform(delete(ENTITY_API_URL_ID, sanctionlist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sanctionlist> sanctionlistList = sanctionlistRepository.findAll();
        assertThat(sanctionlistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
