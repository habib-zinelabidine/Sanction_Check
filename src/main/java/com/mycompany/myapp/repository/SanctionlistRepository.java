package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sanctionlist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sanctionlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SanctionlistRepository extends JpaRepository<Sanctionlist, Long>, JpaSpecificationExecutor<Sanctionlist> {}
