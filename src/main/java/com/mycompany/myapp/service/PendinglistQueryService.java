package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Pendinglist;
import com.mycompany.myapp.repository.PendinglistRepository;
import com.mycompany.myapp.service.criteria.PendinglistCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Pendinglist} entities in the database.
 * The main input is a {@link PendinglistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Pendinglist} or a {@link Page} of {@link Pendinglist} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PendinglistQueryService extends QueryService<Pendinglist> {

    private final Logger log = LoggerFactory.getLogger(PendinglistQueryService.class);

    private final PendinglistRepository pendinglistRepository;

    public PendinglistQueryService(PendinglistRepository pendinglistRepository) {
        this.pendinglistRepository = pendinglistRepository;
    }

    /**
     * Return a {@link List} of {@link Pendinglist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Pendinglist> findByCriteria(PendinglistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pendinglist> specification = createSpecification(criteria);
        return pendinglistRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Pendinglist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Pendinglist> findByCriteria(PendinglistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pendinglist> specification = createSpecification(criteria);
        return pendinglistRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PendinglistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pendinglist> specification = createSpecification(criteria);
        return pendinglistRepository.count(specification);
    }

    /**
     * Function to convert {@link PendinglistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pendinglist> createSpecification(PendinglistCriteria criteria) {
        Specification<Pendinglist> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pendinglist_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Pendinglist_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Pendinglist_.lastName));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Pendinglist_.state));
            }
        }
        return specification;
    }
}
