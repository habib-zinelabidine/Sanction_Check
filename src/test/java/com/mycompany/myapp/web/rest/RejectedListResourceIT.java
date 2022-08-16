package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RejectedList;
import com.mycompany.myapp.repository.RejectedListRepository;
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
 * Integration tests for the {@link RejectedListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RejectedListResourceIT {

    private static final String ENTITY_API_URL = "/api/rejected-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RejectedListRepository rejectedListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRejectedListMockMvc;

    private RejectedList rejectedList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RejectedList createEntity(EntityManager em) {
        RejectedList rejectedList = new RejectedList();
        return rejectedList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RejectedList createUpdatedEntity(EntityManager em) {
        RejectedList rejectedList = new RejectedList();
        return rejectedList;
    }

    @BeforeEach
    public void initTest() {
        rejectedList = createEntity(em);
    }

    @Test
    @Transactional
    void createRejectedList() throws Exception {
        int databaseSizeBeforeCreate = rejectedListRepository.findAll().size();
        // Create the RejectedList
        restRejectedListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rejectedList)))
            .andExpect(status().isCreated());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeCreate + 1);
        RejectedList testRejectedList = rejectedListList.get(rejectedListList.size() - 1);
    }

    @Test
    @Transactional
    void createRejectedListWithExistingId() throws Exception {
        // Create the RejectedList with an existing ID
        rejectedList.setId(1L);

        int databaseSizeBeforeCreate = rejectedListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRejectedListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rejectedList)))
            .andExpect(status().isBadRequest());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRejectedLists() throws Exception {
        // Initialize the database
        rejectedListRepository.saveAndFlush(rejectedList);

        // Get all the rejectedListList
        restRejectedListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rejectedList.getId().intValue())));
    }

    @Test
    @Transactional
    void getRejectedList() throws Exception {
        // Initialize the database
        rejectedListRepository.saveAndFlush(rejectedList);

        // Get the rejectedList
        restRejectedListMockMvc
            .perform(get(ENTITY_API_URL_ID, rejectedList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rejectedList.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRejectedList() throws Exception {
        // Get the rejectedList
        restRejectedListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRejectedList() throws Exception {
        // Initialize the database
        rejectedListRepository.saveAndFlush(rejectedList);

        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();

        // Update the rejectedList
        RejectedList updatedRejectedList = rejectedListRepository.findById(rejectedList.getId()).get();
        // Disconnect from session so that the updates on updatedRejectedList are not directly saved in db
        em.detach(updatedRejectedList);

        restRejectedListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRejectedList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRejectedList))
            )
            .andExpect(status().isOk());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
        RejectedList testRejectedList = rejectedListList.get(rejectedListList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingRejectedList() throws Exception {
        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();
        rejectedList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRejectedListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rejectedList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rejectedList))
            )
            .andExpect(status().isBadRequest());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRejectedList() throws Exception {
        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();
        rejectedList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRejectedListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rejectedList))
            )
            .andExpect(status().isBadRequest());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRejectedList() throws Exception {
        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();
        rejectedList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRejectedListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rejectedList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRejectedListWithPatch() throws Exception {
        // Initialize the database
        rejectedListRepository.saveAndFlush(rejectedList);

        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();

        // Update the rejectedList using partial update
        RejectedList partialUpdatedRejectedList = new RejectedList();
        partialUpdatedRejectedList.setId(rejectedList.getId());

        restRejectedListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRejectedList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRejectedList))
            )
            .andExpect(status().isOk());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
        RejectedList testRejectedList = rejectedListList.get(rejectedListList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateRejectedListWithPatch() throws Exception {
        // Initialize the database
        rejectedListRepository.saveAndFlush(rejectedList);

        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();

        // Update the rejectedList using partial update
        RejectedList partialUpdatedRejectedList = new RejectedList();
        partialUpdatedRejectedList.setId(rejectedList.getId());

        restRejectedListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRejectedList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRejectedList))
            )
            .andExpect(status().isOk());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
        RejectedList testRejectedList = rejectedListList.get(rejectedListList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingRejectedList() throws Exception {
        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();
        rejectedList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRejectedListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rejectedList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rejectedList))
            )
            .andExpect(status().isBadRequest());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRejectedList() throws Exception {
        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();
        rejectedList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRejectedListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rejectedList))
            )
            .andExpect(status().isBadRequest());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRejectedList() throws Exception {
        int databaseSizeBeforeUpdate = rejectedListRepository.findAll().size();
        rejectedList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRejectedListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rejectedList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RejectedList in the database
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRejectedList() throws Exception {
        // Initialize the database
        rejectedListRepository.saveAndFlush(rejectedList);

        int databaseSizeBeforeDelete = rejectedListRepository.findAll().size();

        // Delete the rejectedList
        restRejectedListMockMvc
            .perform(delete(ENTITY_API_URL_ID, rejectedList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RejectedList> rejectedListList = rejectedListRepository.findAll();
        assertThat(rejectedListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
