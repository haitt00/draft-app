package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VAbstractAuditingEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VAbstractAuditingEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VAbstractAuditingEntityRepository extends JpaRepository<VAbstractAuditingEntity, Long> {}
