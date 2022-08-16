package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RejectedList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RejectedList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RejectedListRepository extends JpaRepository<RejectedList, Long> {}
