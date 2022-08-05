package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Sanctionlist;
import com.mycompany.myapp.repository.SanctionlistRepository;
import com.mycompany.myapp.service.criteria.SanctionlistCriteria;
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
 * Service for executing complex queries for {@link Sanctionlist} entities in the database.
 * The main input is a {@link SanctionlistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sanctionlist} or a {@link Page} of {@link Sanctionlist} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SanctionlistQueryService extends QueryService<Sanctionlist> {

    private final Logger log = LoggerFactory.getLogger(SanctionlistQueryService.class);

    private final SanctionlistRepository sanctionlistRepository;

    public SanctionlistQueryService(SanctionlistRepository sanctionlistRepository) {
        this.sanctionlistRepository = sanctionlistRepository;
    }

    /**
     * Return a {@link List} of {@link Sanctionlist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sanctionlist> findByCriteria(SanctionlistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sanctionlist> specification = createSpecification(criteria);
        return sanctionlistRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sanctionlist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sanctionlist> findByCriteria(SanctionlistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sanctionlist> specification = createSpecification(criteria);
        return sanctionlistRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SanctionlistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sanctionlist> specification = createSpecification(criteria);
        return sanctionlistRepository.count(specification);
    }

    /**
     * Function to convert {@link SanctionlistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sanctionlist> createSpecification(SanctionlistCriteria criteria) {
        Specification<Sanctionlist> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sanctionlist_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Sanctionlist_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Sanctionlist_.lastName));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), Sanctionlist_.dob));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Sanctionlist_.address));
            }
            if (criteria.getPassport() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassport(), Sanctionlist_.passport));
            }
            if (criteria.getScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScore(), Sanctionlist_.score));
            }
        }
        return specification;
    }
}
