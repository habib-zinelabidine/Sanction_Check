package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Pendinglist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Pendinglist}.
 */
public interface PendinglistService {
    /**
     * Save a pendinglist.
     *
     * @param pendinglist the entity to save.
     * @return the persisted entity.
     */
    Pendinglist save(Pendinglist pendinglist);

    /**
     * Updates a pendinglist.
     *
     * @param pendinglist the entity to update.
     * @return the persisted entity.
     */
    Pendinglist update(Pendinglist pendinglist);

    /**
     * Partially updates a pendinglist.
     *
     * @param pendinglist the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pendinglist> partialUpdate(Pendinglist pendinglist);

    /**
     * Get all the pendinglists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pendinglist> findAll(Pageable pageable);

    /**
     * Get the "id" pendinglist.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pendinglist> findOne(Long id);

    /**
     * Delete the "id" pendinglist.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
