package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AcceptedPeopleRepository;
import com.mycompany.myapp.service.AcceptedPeopleQueryService;
import com.mycompany.myapp.service.AcceptedPeopleService;
import com.mycompany.myapp.service.criteria.AcceptedPeopleCriteria;
import com.mycompany.myapp.service.dto.AcceptedPeopleDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AcceptedPeople}.
 */
@RestController
@RequestMapping("/api")
public class AcceptedPeopleResource {

    private final Logger log = LoggerFactory.getLogger(AcceptedPeopleResource.class);

    private static final String ENTITY_NAME = "acceptedPeople";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcceptedPeopleService acceptedPeopleService;

    private final AcceptedPeopleRepository acceptedPeopleRepository;

    private final AcceptedPeopleQueryService acceptedPeopleQueryService;

    public AcceptedPeopleResource(
        AcceptedPeopleService acceptedPeopleService,
        AcceptedPeopleRepository acceptedPeopleRepository,
        AcceptedPeopleQueryService acceptedPeopleQueryService
    ) {
        this.acceptedPeopleService = acceptedPeopleService;
        this.acceptedPeopleRepository = acceptedPeopleRepository;
        this.acceptedPeopleQueryService = acceptedPeopleQueryService;
    }

    /**
     * {@code POST  /accepted-people} : Create a new acceptedPeople.
     *
     * @param acceptedPeopleDTO the acceptedPeopleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acceptedPeopleDTO, or with status {@code 400 (Bad Request)} if the acceptedPeople has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accepted-people")
    public ResponseEntity<AcceptedPeopleDTO> createAcceptedPeople(@RequestBody AcceptedPeopleDTO acceptedPeopleDTO)
        throws URISyntaxException {
        log.debug("REST request to save AcceptedPeople : {}", acceptedPeopleDTO);
        if (acceptedPeopleDTO.getId() != null) {
            throw new BadRequestAlertException("A new acceptedPeople cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcceptedPeopleDTO result = acceptedPeopleService.save(acceptedPeopleDTO);
        return ResponseEntity
            .created(new URI("/api/accepted-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /accepted-people/:id} : Updates an existing acceptedPeople.
     *
     * @param id the id of the acceptedPeopleDTO to save.
     * @param acceptedPeopleDTO the acceptedPeopleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acceptedPeopleDTO,
     * or with status {@code 400 (Bad Request)} if the acceptedPeopleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acceptedPeopleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/accepted-people/{id}")
    public ResponseEntity<AcceptedPeopleDTO> updateAcceptedPeople(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AcceptedPeopleDTO acceptedPeopleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AcceptedPeople : {}, {}", id, acceptedPeopleDTO);
        if (acceptedPeopleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acceptedPeopleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acceptedPeopleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AcceptedPeopleDTO result = acceptedPeopleService.update(acceptedPeopleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acceptedPeopleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /accepted-people/:id} : Partial updates given fields of an existing acceptedPeople, field will ignore if it is null
     *
     * @param id the id of the acceptedPeopleDTO to save.
     * @param acceptedPeopleDTO the acceptedPeopleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acceptedPeopleDTO,
     * or with status {@code 400 (Bad Request)} if the acceptedPeopleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the acceptedPeopleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the acceptedPeopleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/accepted-people/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcceptedPeopleDTO> partialUpdateAcceptedPeople(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AcceptedPeopleDTO acceptedPeopleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AcceptedPeople partially : {}, {}", id, acceptedPeopleDTO);
        if (acceptedPeopleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acceptedPeopleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acceptedPeopleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcceptedPeopleDTO> result = acceptedPeopleService.partialUpdate(acceptedPeopleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acceptedPeopleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /accepted-people} : get all the acceptedPeople.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acceptedPeople in body.
     */
    @GetMapping("/accepted-people")
    public ResponseEntity<List<AcceptedPeopleDTO>> getAllAcceptedPeople(
        AcceptedPeopleCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AcceptedPeople by criteria: {}", criteria);
        Page<AcceptedPeopleDTO> page = acceptedPeopleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accepted-people/count} : count all the acceptedPeople.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/accepted-people/count")
    public ResponseEntity<Long> countAcceptedPeople(AcceptedPeopleCriteria criteria) {
        log.debug("REST request to count AcceptedPeople by criteria: {}", criteria);
        return ResponseEntity.ok().body(acceptedPeopleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /accepted-people/:id} : get the "id" acceptedPeople.
     *
     * @param id the id of the acceptedPeopleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acceptedPeopleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/accepted-people/{id}")
    public ResponseEntity<AcceptedPeopleDTO> getAcceptedPeople(@PathVariable Long id) {
        log.debug("REST request to get AcceptedPeople : {}", id);
        Optional<AcceptedPeopleDTO> acceptedPeopleDTO = acceptedPeopleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acceptedPeopleDTO);
    }

    /**
     * {@code DELETE  /accepted-people/:id} : delete the "id" acceptedPeople.
     *
     * @param id the id of the acceptedPeopleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/accepted-people/{id}")
    public ResponseEntity<Void> deleteAcceptedPeople(@PathVariable Long id) {
        log.debug("REST request to delete AcceptedPeople : {}", id);
        acceptedPeopleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
