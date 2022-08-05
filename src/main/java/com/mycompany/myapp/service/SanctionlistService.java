package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Sanctionlist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Sanctionlist}.
 */
public interface SanctionlistService {
    /**
     * Save a sanctionlist.
     *
     * @param sanctionlist the entity to save.
     * @return the persisted entity.
     */
    Sanctionlist save(Sanctionlist sanctionlist);

    /**
     * Updates a sanctionlist.
     *
     * @param sanctionlist the entity to update.
     * @return the persisted entity.
     */
    Sanctionlist update(Sanctionlist sanctionlist);

    /**
     * Partially updates a sanctionlist.
     *
     * @param sanctionlist the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sanctionlist> partialUpdate(Sanctionlist sanctionlist);

    /**
     * Get all the sanctionlists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sanctionlist> findAll(Pageable pageable);

    /**
     * Get the "id" sanctionlist.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sanctionlist> findOne(Long id);

    /**
     * Delete the "id" sanctionlist.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
