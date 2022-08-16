package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Accepted;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Accepted}.
 */
public interface AcceptedService {
    /**
     * Save a accepted.
     *
     * @param accepted the entity to save.
     * @return the persisted entity.
     */
    Accepted save(Accepted accepted);

    /**
     * Updates a accepted.
     *
     * @param accepted the entity to update.
     * @return the persisted entity.
     */
    Accepted update(Accepted accepted);

    /**
     * Partially updates a accepted.
     *
     * @param accepted the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Accepted> partialUpdate(Accepted accepted);

    /**
     * Get all the accepteds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Accepted> findAll(Pageable pageable);

    /**
     * Get the "id" accepted.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Accepted> findOne(Long id);

    /**
     * Delete the "id" accepted.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
