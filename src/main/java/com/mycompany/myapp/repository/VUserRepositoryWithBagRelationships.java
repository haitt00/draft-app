package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface VUserRepositoryWithBagRelationships {
    Optional<VUser> fetchBagRelationships(Optional<VUser> vUser);

    List<VUser> fetchBagRelationships(List<VUser> vUsers);

    Page<VUser> fetchBagRelationships(Page<VUser> vUsers);
}
