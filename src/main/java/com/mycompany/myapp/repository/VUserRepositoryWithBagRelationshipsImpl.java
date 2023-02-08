package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VUser;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class VUserRepositoryWithBagRelationshipsImpl implements VUserRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<VUser> fetchBagRelationships(Optional<VUser> vUser) {
        return vUser.map(this::fetchVRoles);
    }

    @Override
    public Page<VUser> fetchBagRelationships(Page<VUser> vUsers) {
        return new PageImpl<>(fetchBagRelationships(vUsers.getContent()), vUsers.getPageable(), vUsers.getTotalElements());
    }

    @Override
    public List<VUser> fetchBagRelationships(List<VUser> vUsers) {
        return Optional.of(vUsers).map(this::fetchVRoles).orElse(Collections.emptyList());
    }

    VUser fetchVRoles(VUser result) {
        return entityManager
            .createQuery("select vUser from VUser vUser left join fetch vUser.vRoles where vUser is :vUser", VUser.class)
            .setParameter("vUser", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<VUser> fetchVRoles(List<VUser> vUsers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, vUsers.size()).forEach(index -> order.put(vUsers.get(index).getId(), index));
        List<VUser> result = entityManager
            .createQuery("select distinct vUser from VUser vUser left join fetch vUser.vRoles where vUser in :vUsers", VUser.class)
            .setParameter("vUsers", vUsers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
