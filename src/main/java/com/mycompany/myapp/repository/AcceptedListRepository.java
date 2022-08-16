package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AcceptedList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AcceptedList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcceptedListRepository extends JpaRepository<AcceptedList, Long> {}
