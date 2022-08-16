package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RejectedList;
import com.mycompany.myapp.repository.RejectedListRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RejectedList}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RejectedListResource {

    private final Logger log = LoggerFactory.getLogger(RejectedListResource.class);

    private static final String ENTITY_NAME = "rejectedList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RejectedListRepository rejectedListRepository;

    public RejectedListResource(RejectedListRepository rejectedListRepository) {
        this.rejectedListRepository = rejectedListRepository;
    }

    /**
     * {@code POST  /rejected-lists} : Create a new rejectedList.
     *
     * @param rejectedList the rejectedList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rejectedList, or with status {@code 400 (Bad Request)} if the rejectedList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rejected-lists")
    public ResponseEntity<RejectedList> createRejectedList(@RequestBody RejectedList rejectedList) throws URISyntaxException {
        log.debug("REST request to save RejectedList : {}", rejectedList);
        if (rejectedList.getId() != null) {
            throw new BadRequestAlertException("A new rejectedList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RejectedList result = rejectedListRepository.save(rejectedList);
        return ResponseEntity
            .created(new URI("/api/rejected-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rejected-lists/:id} : Updates an existing rejectedList.
     *
     * @param id the id of the rejectedList to save.
     * @param rejectedList the rejectedList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rejectedList,
     * or with status {@code 400 (Bad Request)} if the rejectedList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rejectedList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rejected-lists/{id}")
    public ResponseEntity<RejectedList> updateRejectedList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RejectedList rejectedList
    ) throws URISyntaxException {
        log.debug("REST request to update RejectedList : {}, {}", id, rejectedList);
        if (rejectedList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rejectedList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rejectedListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // no save call needed as we have no fields that can be updated
        RejectedList result = rejectedList;
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rejectedList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rejected-lists/:id} : Partial updates given fields of an existing rejectedList, field will ignore if it is null
     *
     * @param id the id of the rejectedList to save.
     * @param rejectedList the rejectedList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rejectedList,
     * or with status {@code 400 (Bad Request)} if the rejectedList is not valid,
     * or with status {@code 404 (Not Found)} if the rejectedList is not found,
     * or with status {@code 500 (Internal Server Error)} if the rejectedList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rejected-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RejectedList> partialUpdateRejectedList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RejectedList rejectedList
    ) throws URISyntaxException {
        log.debug("REST request to partial update RejectedList partially : {}, {}", id, rejectedList);
        if (rejectedList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rejectedList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rejectedListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RejectedList> result = rejectedListRepository
            .findById(rejectedList.getId())
            .map(existingRejectedList -> {
                return existingRejectedList;
            }); // .map(rejectedListRepository::save)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rejectedList.getId().toString())
        );
    }

    /**
     * {@code GET  /rejected-lists} : get all the rejectedLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rejectedLists in body.
     */
    @GetMapping("/rejected-lists")
    public List<RejectedList> getAllRejectedLists() {
        log.debug("REST request to get all RejectedLists");
        return rejectedListRepository.findAll();
    }

    /**
     * {@code GET  /rejected-lists/:id} : get the "id" rejectedList.
     *
     * @param id the id of the rejectedList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rejectedList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rejected-lists/{id}")
    public ResponseEntity<RejectedList> getRejectedList(@PathVariable Long id) {
        log.debug("REST request to get RejectedList : {}", id);
        Optional<RejectedList> rejectedList = rejectedListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rejectedList);
    }

    /**
     * {@code DELETE  /rejected-lists/:id} : delete the "id" rejectedList.
     *
     * @param id the id of the rejectedList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rejected-lists/{id}")
    public ResponseEntity<Void> deleteRejectedList(@PathVariable Long id) {
        log.debug("REST request to delete RejectedList : {}", id);
        rejectedListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
