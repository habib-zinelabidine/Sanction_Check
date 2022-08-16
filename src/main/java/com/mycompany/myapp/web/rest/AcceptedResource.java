package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Accepted;
import com.mycompany.myapp.repository.AcceptedRepository;
import com.mycompany.myapp.service.AcceptedService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Accepted}.
 */
@RestController
@RequestMapping("/api")
public class AcceptedResource {

    private final Logger log = LoggerFactory.getLogger(AcceptedResource.class);

    private static final String ENTITY_NAME = "accepted";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcceptedService acceptedService;

    private final AcceptedRepository acceptedRepository;

    public AcceptedResource(AcceptedService acceptedService, AcceptedRepository acceptedRepository) {
        this.acceptedService = acceptedService;
        this.acceptedRepository = acceptedRepository;
    }

    /**
     * {@code POST  /accepteds} : Create a new accepted.
     *
     * @param accepted the accepted to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accepted, or with status {@code 400 (Bad Request)} if the accepted has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accepteds")
    public ResponseEntity<Accepted> createAccepted(@RequestBody Accepted accepted) throws URISyntaxException {
        log.debug("REST request to save Accepted : {}", accepted);
        if (accepted.getId() != null) {
            throw new BadRequestAlertException("A new accepted cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Accepted result = acceptedService.save(accepted);
        return ResponseEntity
            .created(new URI("/api/accepteds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /accepteds/:id} : Updates an existing accepted.
     *
     * @param id the id of the accepted to save.
     * @param accepted the accepted to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accepted,
     * or with status {@code 400 (Bad Request)} if the accepted is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accepted couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/accepteds/{id}")
    public ResponseEntity<Accepted> updateAccepted(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Accepted accepted
    ) throws URISyntaxException {
        log.debug("REST request to update Accepted : {}, {}", id, accepted);
        if (accepted.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accepted.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acceptedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Accepted result = acceptedService.update(accepted);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accepted.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /accepteds/:id} : Partial updates given fields of an existing accepted, field will ignore if it is null
     *
     * @param id the id of the accepted to save.
     * @param accepted the accepted to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accepted,
     * or with status {@code 400 (Bad Request)} if the accepted is not valid,
     * or with status {@code 404 (Not Found)} if the accepted is not found,
     * or with status {@code 500 (Internal Server Error)} if the accepted couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/accepteds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Accepted> partialUpdateAccepted(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Accepted accepted
    ) throws URISyntaxException {
        log.debug("REST request to partial update Accepted partially : {}, {}", id, accepted);
        if (accepted.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accepted.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acceptedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Accepted> result = acceptedService.partialUpdate(accepted);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accepted.getId().toString())
        );
    }

    /**
     * {@code GET  /accepteds} : get all the accepteds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accepteds in body.
     */
    @GetMapping("/accepteds")
    public ResponseEntity<List<Accepted>> getAllAccepteds(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Accepteds");
        Page<Accepted> page = acceptedService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accepteds/:id} : get the "id" accepted.
     *
     * @param id the id of the accepted to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accepted, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/accepteds/{id}")
    public ResponseEntity<Accepted> getAccepted(@PathVariable Long id) {
        log.debug("REST request to get Accepted : {}", id);
        Optional<Accepted> accepted = acceptedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accepted);
    }

    /**
     * {@code DELETE  /accepteds/:id} : delete the "id" accepted.
     *
     * @param id the id of the accepted to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/accepteds/{id}")
    public ResponseEntity<Void> deleteAccepted(@PathVariable Long id) {
        log.debug("REST request to delete Accepted : {}", id);
        acceptedService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
