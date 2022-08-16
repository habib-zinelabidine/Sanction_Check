package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.AcceptedPeople;
import com.mycompany.myapp.repository.AcceptedPeopleRepository;
import com.mycompany.myapp.service.criteria.AcceptedPeopleCriteria;
import com.mycompany.myapp.service.dto.AcceptedPeopleDTO;
import com.mycompany.myapp.service.mapper.AcceptedPeopleMapper;
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
 * Service for executing complex queries for {@link AcceptedPeople} entities in the database.
 * The main input is a {@link AcceptedPeopleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AcceptedPeopleDTO} or a {@link Page} of {@link AcceptedPeopleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AcceptedPeopleQueryService extends QueryService<AcceptedPeople> {

    private final Logger log = LoggerFactory.getLogger(AcceptedPeopleQueryService.class);

    private final AcceptedPeopleRepository acceptedPeopleRepository;

    private final AcceptedPeopleMapper acceptedPeopleMapper;

    public AcceptedPeopleQueryService(AcceptedPeopleRepository acceptedPeopleRepository, AcceptedPeopleMapper acceptedPeopleMapper) {
        this.acceptedPeopleRepository = acceptedPeopleRepository;
        this.acceptedPeopleMapper = acceptedPeopleMapper;
    }

    /**
     * Return a {@link List} of {@link AcceptedPeopleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AcceptedPeopleDTO> findByCriteria(AcceptedPeopleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AcceptedPeople> specification = createSpecification(criteria);
        return acceptedPeopleMapper.toDto(acceptedPeopleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AcceptedPeopleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AcceptedPeopleDTO> findByCriteria(AcceptedPeopleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AcceptedPeople> specification = createSpecification(criteria);
        return acceptedPeopleRepository.findAll(specification, page).map(acceptedPeopleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AcceptedPeopleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AcceptedPeople> specification = createSpecification(criteria);
        return acceptedPeopleRepository.count(specification);
    }

    /**
     * Function to convert {@link AcceptedPeopleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AcceptedPeople> createSpecification(AcceptedPeopleCriteria criteria) {
        Specification<AcceptedPeople> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AcceptedPeople_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), AcceptedPeople_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), AcceptedPeople_.lastName));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AcceptedPeople_.status));
            }
        }
        return specification;
    }
}
