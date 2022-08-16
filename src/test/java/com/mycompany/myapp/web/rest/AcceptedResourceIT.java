package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Accepted;
import com.mycompany.myapp.repository.AcceptedRepository;
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
 * Integration tests for the {@link AcceptedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcceptedResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/accepteds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcceptedRepository acceptedRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcceptedMockMvc;

    private Accepted accepted;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accepted createEntity(EntityManager em) {
        Accepted accepted = new Accepted().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
        return accepted;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accepted createUpdatedEntity(EntityManager em) {
        Accepted accepted = new Accepted().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        return accepted;
    }

    @BeforeEach
    public void initTest() {
        accepted = createEntity(em);
    }

    @Test
    @Transactional
    void createAccepted() throws Exception {
        int databaseSizeBeforeCreate = acceptedRepository.findAll().size();
        // Create the Accepted
        restAcceptedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accepted)))
            .andExpect(status().isCreated());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeCreate + 1);
        Accepted testAccepted = acceptedList.get(acceptedList.size() - 1);
        assertThat(testAccepted.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAccepted.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void createAcceptedWithExistingId() throws Exception {
        // Create the Accepted with an existing ID
        accepted.setId(1L);

        int databaseSizeBeforeCreate = acceptedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcceptedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accepted)))
            .andExpect(status().isBadRequest());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccepteds() throws Exception {
        // Initialize the database
        acceptedRepository.saveAndFlush(accepted);

        // Get all the acceptedList
        restAcceptedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accepted.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @Test
    @Transactional
    void getAccepted() throws Exception {
        // Initialize the database
        acceptedRepository.saveAndFlush(accepted);

        // Get the accepted
        restAcceptedMockMvc
            .perform(get(ENTITY_API_URL_ID, accepted.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accepted.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAccepted() throws Exception {
        // Get the accepted
        restAcceptedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccepted() throws Exception {
        // Initialize the database
        acceptedRepository.saveAndFlush(accepted);

        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();

        // Update the accepted
        Accepted updatedAccepted = acceptedRepository.findById(accepted.getId()).get();
        // Disconnect from session so that the updates on updatedAccepted are not directly saved in db
        em.detach(updatedAccepted);
        updatedAccepted.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restAcceptedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccepted.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccepted))
            )
            .andExpect(status().isOk());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
        Accepted testAccepted = acceptedList.get(acceptedList.size() - 1);
        assertThat(testAccepted.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAccepted.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAccepted() throws Exception {
        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();
        accepted.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcceptedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accepted.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accepted))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccepted() throws Exception {
        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();
        accepted.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accepted))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccepted() throws Exception {
        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();
        accepted.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accepted)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcceptedWithPatch() throws Exception {
        // Initialize the database
        acceptedRepository.saveAndFlush(accepted);

        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();

        // Update the accepted using partial update
        Accepted partialUpdatedAccepted = new Accepted();
        partialUpdatedAccepted.setId(accepted.getId());

        restAcceptedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccepted.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccepted))
            )
            .andExpect(status().isOk());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
        Accepted testAccepted = acceptedList.get(acceptedList.size() - 1);
        assertThat(testAccepted.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAccepted.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAcceptedWithPatch() throws Exception {
        // Initialize the database
        acceptedRepository.saveAndFlush(accepted);

        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();

        // Update the accepted using partial update
        Accepted partialUpdatedAccepted = new Accepted();
        partialUpdatedAccepted.setId(accepted.getId());

        partialUpdatedAccepted.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restAcceptedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccepted.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccepted))
            )
            .andExpect(status().isOk());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
        Accepted testAccepted = acceptedList.get(acceptedList.size() - 1);
        assertThat(testAccepted.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAccepted.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAccepted() throws Exception {
        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();
        accepted.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcceptedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accepted.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accepted))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccepted() throws Exception {
        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();
        accepted.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accepted))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccepted() throws Exception {
        int databaseSizeBeforeUpdate = acceptedRepository.findAll().size();
        accepted.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accepted)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accepted in the database
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccepted() throws Exception {
        // Initialize the database
        acceptedRepository.saveAndFlush(accepted);

        int databaseSizeBeforeDelete = acceptedRepository.findAll().size();

        // Delete the accepted
        restAcceptedMockMvc
            .perform(delete(ENTITY_API_URL_ID, accepted.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Accepted> acceptedList = acceptedRepository.findAll();
        assertThat(acceptedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
