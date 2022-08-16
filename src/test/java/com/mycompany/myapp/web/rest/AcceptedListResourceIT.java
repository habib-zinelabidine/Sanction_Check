package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AcceptedList;
import com.mycompany.myapp.repository.AcceptedListRepository;
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
 * Integration tests for the {@link AcceptedListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcceptedListResourceIT {

    private static final String ENTITY_API_URL = "/api/accepted-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcceptedListRepository acceptedListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcceptedListMockMvc;

    private AcceptedList acceptedList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcceptedList createEntity(EntityManager em) {
        AcceptedList acceptedList = new AcceptedList();
        return acceptedList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcceptedList createUpdatedEntity(EntityManager em) {
        AcceptedList acceptedList = new AcceptedList();
        return acceptedList;
    }

    @BeforeEach
    public void initTest() {
        acceptedList = createEntity(em);
    }

    @Test
    @Transactional
    void createAcceptedList() throws Exception {
        int databaseSizeBeforeCreate = acceptedListRepository.findAll().size();
        // Create the AcceptedList
        restAcceptedListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptedList)))
            .andExpect(status().isCreated());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeCreate + 1);
        AcceptedList testAcceptedList = acceptedListList.get(acceptedListList.size() - 1);
    }

    @Test
    @Transactional
    void createAcceptedListWithExistingId() throws Exception {
        // Create the AcceptedList with an existing ID
        acceptedList.setId(1L);

        int databaseSizeBeforeCreate = acceptedListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcceptedListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptedList)))
            .andExpect(status().isBadRequest());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAcceptedLists() throws Exception {
        // Initialize the database
        acceptedListRepository.saveAndFlush(acceptedList);

        // Get all the acceptedListList
        restAcceptedListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acceptedList.getId().intValue())));
    }

    @Test
    @Transactional
    void getAcceptedList() throws Exception {
        // Initialize the database
        acceptedListRepository.saveAndFlush(acceptedList);

        // Get the acceptedList
        restAcceptedListMockMvc
            .perform(get(ENTITY_API_URL_ID, acceptedList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acceptedList.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAcceptedList() throws Exception {
        // Get the acceptedList
        restAcceptedListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcceptedList() throws Exception {
        // Initialize the database
        acceptedListRepository.saveAndFlush(acceptedList);

        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();

        // Update the acceptedList
        AcceptedList updatedAcceptedList = acceptedListRepository.findById(acceptedList.getId()).get();
        // Disconnect from session so that the updates on updatedAcceptedList are not directly saved in db
        em.detach(updatedAcceptedList);

        restAcceptedListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAcceptedList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAcceptedList))
            )
            .andExpect(status().isOk());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
        AcceptedList testAcceptedList = acceptedListList.get(acceptedListList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingAcceptedList() throws Exception {
        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();
        acceptedList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcceptedListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acceptedList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acceptedList))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcceptedList() throws Exception {
        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();
        acceptedList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acceptedList))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcceptedList() throws Exception {
        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();
        acceptedList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptedList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcceptedListWithPatch() throws Exception {
        // Initialize the database
        acceptedListRepository.saveAndFlush(acceptedList);

        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();

        // Update the acceptedList using partial update
        AcceptedList partialUpdatedAcceptedList = new AcceptedList();
        partialUpdatedAcceptedList.setId(acceptedList.getId());

        restAcceptedListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcceptedList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcceptedList))
            )
            .andExpect(status().isOk());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
        AcceptedList testAcceptedList = acceptedListList.get(acceptedListList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateAcceptedListWithPatch() throws Exception {
        // Initialize the database
        acceptedListRepository.saveAndFlush(acceptedList);

        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();

        // Update the acceptedList using partial update
        AcceptedList partialUpdatedAcceptedList = new AcceptedList();
        partialUpdatedAcceptedList.setId(acceptedList.getId());

        restAcceptedListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcceptedList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcceptedList))
            )
            .andExpect(status().isOk());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
        AcceptedList testAcceptedList = acceptedListList.get(acceptedListList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingAcceptedList() throws Exception {
        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();
        acceptedList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcceptedListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acceptedList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acceptedList))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcceptedList() throws Exception {
        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();
        acceptedList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acceptedList))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcceptedList() throws Exception {
        int databaseSizeBeforeUpdate = acceptedListRepository.findAll().size();
        acceptedList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptedListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(acceptedList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcceptedList in the database
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcceptedList() throws Exception {
        // Initialize the database
        acceptedListRepository.saveAndFlush(acceptedList);

        int databaseSizeBeforeDelete = acceptedListRepository.findAll().size();

        // Delete the acceptedList
        restAcceptedListMockMvc
            .perform(delete(ENTITY_API_URL_ID, acceptedList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcceptedList> acceptedListList = acceptedListRepository.findAll();
        assertThat(acceptedListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
