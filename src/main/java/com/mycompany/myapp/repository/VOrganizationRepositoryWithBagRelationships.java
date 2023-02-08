package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VOrganization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface VOrganizationRepositoryWithBagRelationships {
    Optional<VOrganization> fetchBagRelationships(Optional<VOrganization> vOrganization);

    List<VOrganization> fetchBagRelationships(List<VOrganization> vOrganizations);

    Page<VOrganization> fetchBagRelationships(Page<VOrganization> vOrganizations);
}
