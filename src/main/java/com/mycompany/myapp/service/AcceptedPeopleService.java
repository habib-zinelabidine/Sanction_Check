package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AcceptedPeopleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AcceptedPeople}.
 */
public interface AcceptedPeopleService {
    /**
     * Save a acceptedPeople.
     *
     * @param acceptedPeopleDTO the entity to save.
     * @return the persisted entity.
     */
    AcceptedPeopleDTO save(AcceptedPeopleDTO acceptedPeopleDTO);

    /**
     * Updates a acceptedPeople.
     *
     * @param acceptedPeopleDTO the entity to update.
     * @return the persisted entity.
     */
    AcceptedPeopleDTO update(AcceptedPeopleDTO acceptedPeopleDTO);

    /**
     * Partially updates a acceptedPeople.
     *
     * @param acceptedPeopleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AcceptedPeopleDTO> partialUpdate(AcceptedPeopleDTO acceptedPeopleDTO);

    /**
     * Get all the acceptedPeople.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AcceptedPeopleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" acceptedPeople.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AcceptedPeopleDTO> findOne(Long id);

    /**
     * Delete the "id" acceptedPeople.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
