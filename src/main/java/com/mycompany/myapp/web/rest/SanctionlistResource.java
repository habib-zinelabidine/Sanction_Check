package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Sanctionlist;
import com.mycompany.myapp.repository.SanctionlistRepository;
import com.mycompany.myapp.service.SanctionlistQueryService;
import com.mycompany.myapp.service.SanctionlistService;
import com.mycompany.myapp.service.criteria.SanctionlistCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Sanctionlist}.
 */
@RestController
@RequestMapping("/api")
public class SanctionlistResource {

    private final Logger log = LoggerFactory.getLogger(SanctionlistResource.class);

    private static final String ENTITY_NAME = "sanctionlist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SanctionlistService sanctionlistService;

    private final SanctionlistRepository sanctionlistRepository;

    private final SanctionlistQueryService sanctionlistQueryService;

    public SanctionlistResource(
        SanctionlistService sanctionlistService,
        SanctionlistRepository sanctionlistRepository,
        SanctionlistQueryService sanctionlistQueryService
    ) {
        this.sanctionlistService = sanctionlistService;
        this.sanctionlistRepository = sanctionlistRepository;
        this.sanctionlistQueryService = sanctionlistQueryService;
    }

    /**
     * {@code POST  /sanctionlists} : Create a new sanctionlist.
     *
     * @param sanctionlist the sanctionlist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sanctionlist, or with status {@code 400 (Bad Request)} if the sanctionlist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sanctionlists")
    public ResponseEntity<Sanctionlist> createSanctionlist(@RequestBody Sanctionlist sanctionlist) throws URISyntaxException {
        log.debug("REST request to save Sanctionlist : {}", sanctionlist);
        if (sanctionlist.getId() != null) {
            throw new BadRequestAlertException("A new sanctionlist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sanctionlist result = sanctionlistService.save(sanctionlist);
        return ResponseEntity
            .created(new URI("/api/sanctionlists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sanctionlists/:id} : Updates an existing sanctionlist.
     *
     * @param id the id of the sanctionlist to save.
     * @param sanctionlist the sanctionlist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanctionlist,
     * or with status {@code 400 (Bad Request)} if the sanctionlist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sanctionlist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sanctionlists/{id}")
    public ResponseEntity<Sanctionlist> updateSanctionlist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sanctionlist sanctionlist
    ) throws URISyntaxException {
        log.debug("REST request to update Sanctionlist : {}, {}", id, sanctionlist);
        if (sanctionlist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanctionlist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionlistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sanctionlist result = sanctionlistService.update(sanctionlist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sanctionlist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sanctionlists/:id} : Partial updates given fields of an existing sanctionlist, field will ignore if it is null
     *
     * @param id the id of the sanctionlist to save.
     * @param sanctionlist the sanctionlist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sanctionlist,
     * or with status {@code 400 (Bad Request)} if the sanctionlist is not valid,
     * or with status {@code 404 (Not Found)} if the sanctionlist is not found,
     * or with status {@code 500 (Internal Server Error)} if the sanctionlist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sanctionlists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sanctionlist> partialUpdateSanctionlist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sanctionlist sanctionlist
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sanctionlist partially : {}, {}", id, sanctionlist);
        if (sanctionlist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sanctionlist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sanctionlistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sanctionlist> result = sanctionlistService.partialUpdate(sanctionlist);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sanctionlist.getId().toString())
        );
    }

    /**
     * {@code GET  /sanctionlists} : get all the sanctionlists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sanctionlists in body.
     */
    @GetMapping("/sanctionlists")
    public ResponseEntity<List<Sanctionlist>> getAllSanctionlists(
        SanctionlistCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Sanctionlists by criteria: {}", criteria);
        Page<Sanctionlist> page = sanctionlistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sanctionlists/count} : count all the sanctionlists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sanctionlists/count")
    public ResponseEntity<Long> countSanctionlists(SanctionlistCriteria criteria) {
        log.debug("REST request to count Sanctionlists by criteria: {}", criteria);
        return ResponseEntity.ok().body(sanctionlistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sanctionlists/:id} : get the "id" sanctionlist.
     *
     * @param id the id of the sanctionlist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sanctionlist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sanctionlists/{id}")
    public ResponseEntity<Sanctionlist> getSanctionlist(@PathVariable Long id) {
        log.debug("REST request to get Sanctionlist : {}", id);
        Optional<Sanctionlist> sanctionlist = sanctionlistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sanctionlist);
    }

    /**
     * {@code DELETE  /sanctionlists/:id} : delete the "id" sanctionlist.
     *
     * @param id the id of the sanctionlist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sanctionlists/{id}")
    public ResponseEntity<Void> deleteSanctionlist(@PathVariable Long id) {
        log.debug("REST request to delete Sanctionlist : {}", id);
        sanctionlistService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
