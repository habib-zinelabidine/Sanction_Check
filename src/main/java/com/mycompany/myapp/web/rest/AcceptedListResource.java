package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AcceptedList;
import com.mycompany.myapp.repository.AcceptedListRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AcceptedList}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AcceptedListResource {

    private final Logger log = LoggerFactory.getLogger(AcceptedListResource.class);

    private static final String ENTITY_NAME = "acceptedList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcceptedListRepository acceptedListRepository;

    public AcceptedListResource(AcceptedListRepository acceptedListRepository) {
        this.acceptedListRepository = acceptedListRepository;
    }

    /**
     * {@code POST  /accepted-lists} : Create a new acceptedList.
     *
     * @param acceptedList the acceptedList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acceptedList, or with status {@code 400 (Bad Request)} if the acceptedList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accepted-lists")
    public ResponseEntity<AcceptedList> createAcceptedList(@RequestBody AcceptedList acceptedList) throws URISyntaxException {
        log.debug("REST request to save AcceptedList : {}", acceptedList);
        if (acceptedList.getId() != null) {
            throw new BadRequestAlertException("A new acceptedList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcceptedList result = acceptedListRepository.save(acceptedList);
        return ResponseEntity
            .created(new URI("/api/accepted-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /accepted-lists/:id} : Updates an existing acceptedList.
     *
     * @param id the id of the acceptedList to save.
     * @param acceptedList the acceptedList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acceptedList,
     * or with status {@code 400 (Bad Request)} if the acceptedList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acceptedList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/accepted-lists/{id}")
    public ResponseEntity<AcceptedList> updateAcceptedList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AcceptedList acceptedList
    ) throws URISyntaxException {
        log.debug("REST request to update AcceptedList : {}, {}", id, acceptedList);
        if (acceptedList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acceptedList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acceptedListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // no save call needed as we have no fields that can be updated
        AcceptedList result = acceptedList;
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acceptedList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /accepted-lists/:id} : Partial updates given fields of an existing acceptedList, field will ignore if it is null
     *
     * @param id the id of the acceptedList to save.
     * @param acceptedList the acceptedList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acceptedList,
     * or with status {@code 400 (Bad Request)} if the acceptedList is not valid,
     * or with status {@code 404 (Not Found)} if the acceptedList is not found,
     * or with status {@code 500 (Internal Server Error)} if the acceptedList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/accepted-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcceptedList> partialUpdateAcceptedList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AcceptedList acceptedList
    ) throws URISyntaxException {
        log.debug("REST request to partial update AcceptedList partially : {}, {}", id, acceptedList);
        if (acceptedList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acceptedList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acceptedListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcceptedList> result = acceptedListRepository
            .findById(acceptedList.getId())
            .map(existingAcceptedList -> {
                return existingAcceptedList;
            }); // .map(acceptedListRepository::save)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acceptedList.getId().toString())
        );
    }

    /**
     * {@code GET  /accepted-lists} : get all the acceptedLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acceptedLists in body.
     */
    @GetMapping("/accepted-lists")
    public List<AcceptedList> getAllAcceptedLists() {
        log.debug("REST request to get all AcceptedLists");
        return acceptedListRepository.findAll();
    }

    /**
     * {@code GET  /accepted-lists/:id} : get the "id" acceptedList.
     *
     * @param id the id of the acceptedList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acceptedList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/accepted-lists/{id}")
    public ResponseEntity<AcceptedList> getAcceptedList(@PathVariable Long id) {
        log.debug("REST request to get AcceptedList : {}", id);
        Optional<AcceptedList> acceptedList = acceptedListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(acceptedList);
    }

    /**
     * {@code DELETE  /accepted-lists/:id} : delete the "id" acceptedList.
     *
     * @param id the id of the acceptedList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/accepted-lists/{id}")
    public ResponseEntity<Void> deleteAcceptedList(@PathVariable Long id) {
        log.debug("REST request to delete AcceptedList : {}", id);
        acceptedListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
