package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AcceptedPeople;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AcceptedPeople entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcceptedPeopleRepository extends JpaRepository<AcceptedPeople, Long>, JpaSpecificationExecutor<AcceptedPeople> {}
