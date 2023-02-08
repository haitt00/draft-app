package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VRoleRepository extends JpaRepository<VRole, Long> {}
