package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pendinglist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pendinglist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PendinglistRepository extends JpaRepository<Pendinglist, Long>, JpaSpecificationExecutor<Pendinglist> {}
