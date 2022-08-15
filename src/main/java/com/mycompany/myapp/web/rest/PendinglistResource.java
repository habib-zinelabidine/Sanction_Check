package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Pendinglist;
import com.mycompany.myapp.repository.PendinglistRepository;
import com.mycompany.myapp.service.PendinglistQueryService;
import com.mycompany.myapp.service.PendinglistService;
import com.mycompany.myapp.service.criteria.PendinglistCriteria;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Pendinglist}.
 */
@RestController
@RequestMapping("/api")
public class PendinglistResource {

    private final Logger log = LoggerFactory.getLogger(PendinglistResource.class);

    private static final String ENTITY_NAME = "pendinglist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PendinglistService pendinglistService;

    private final PendinglistRepository pendinglistRepository;

    private final PendinglistQueryService pendinglistQueryService;

    public PendinglistResource(
        PendinglistService pendinglistService,
        PendinglistRepository pendinglistRepository,
        PendinglistQueryService pendinglistQueryService
    ) {
        this.pendinglistService = pendinglistService;
        this.pendinglistRepository = pendinglistRepository;
        this.pendinglistQueryService = pendinglistQueryService;
    }

    /**
     * {@code POST  /pendinglists} : Create a new pendinglist.
     *
     * @param pendinglist the pendinglist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pendinglist, or with status {@code 400 (Bad Request)} if the pendinglist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pendinglists")
    public ResponseEntity<Pendinglist> createPendinglist(@RequestBody Pendinglist pendinglist) throws URISyntaxException {
        log.debug("REST request to save Pendinglist : {}", pendinglist);
        if (pendinglist.getId() != null) {
            throw new BadRequestAlertException("A new pendinglist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pendinglist result = pendinglistService.save(pendinglist);
        return ResponseEntity
            .created(new URI("/api/pendinglists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pendinglists/:id} : Updates an existing pendinglist.
     *
     * @param id the id of the pendinglist to save.
     * @param pendinglist the pendinglist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pendinglist,
     * or with status {@code 400 (Bad Request)} if the pendinglist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pendinglist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pendinglists/{id}")
    public ResponseEntity<Pendinglist> updatePendinglist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pendinglist pendinglist
    ) throws URISyntaxException {
        log.debug("REST request to update Pendinglist : {}, {}", id, pendinglist);
        if (pendinglist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pendinglist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pendinglistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pendinglist result = pendinglistService.update(pendinglist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pendinglist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pendinglists/:id} : Partial updates given fields of an existing pendinglist, field will ignore if it is null
     *
     * @param id the id of the pendinglist to save.
     * @param pendinglist the pendinglist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pendinglist,
     * or with status {@code 400 (Bad Request)} if the pendinglist is not valid,
     * or with status {@code 404 (Not Found)} if the pendinglist is not found,
     * or with status {@code 500 (Internal Server Error)} if the pendinglist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pendinglists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pendinglist> partialUpdatePendinglist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pendinglist pendinglist
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pendinglist partially : {}, {}", id, pendinglist);
        if (pendinglist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pendinglist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pendinglistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pendinglist> result = pendinglistService.partialUpdate(pendinglist);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pendinglist.getId().toString())
        );
    }

    /**
     * {@code GET  /pendinglists} : get all the pendinglists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pendinglists in body.
     */
    @GetMapping("/pendinglists")
    public ResponseEntity<List<Pendinglist>> getAllPendinglists(
        PendinglistCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Pendinglists by criteria: {}", criteria);
        Page<Pendinglist> page = pendinglistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pendinglists/count} : count all the pendinglists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pendinglists/count")
    public ResponseEntity<Long> countPendinglists(PendinglistCriteria criteria) {
        log.debug("REST request to count Pendinglists by criteria: {}", criteria);
        return ResponseEntity.ok().body(pendinglistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pendinglists/:id} : get the "id" pendinglist.
     *
     * @param id the id of the pendinglist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pendinglist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pendinglists/{id}")
    public ResponseEntity<Pendinglist> getPendinglist(@PathVariable Long id) {
        log.debug("REST request to get Pendinglist : {}", id);
        Optional<Pendinglist> pendinglist = pendinglistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pendinglist);
    }

    /**
     * {@code DELETE  /pendinglists/:id} : delete the "id" pendinglist.
     *
     * @param id the id of the pendinglist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pendinglists/{id}")
    public ResponseEntity<Void> deletePendinglist(@PathVariable Long id) {
        log.debug("REST request to delete Pendinglist : {}", id);
        pendinglistService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
