package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VOrganization;
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
public class VOrganizationRepositoryWithBagRelationshipsImpl implements VOrganizationRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<VOrganization> fetchBagRelationships(Optional<VOrganization> vOrganization) {
        return vOrganization.map(this::fetchVUsers).map(this::fetchVRoles);
    }

    @Override
    public Page<VOrganization> fetchBagRelationships(Page<VOrganization> vOrganizations) {
        return new PageImpl<>(
            fetchBagRelationships(vOrganizations.getContent()),
            vOrganizations.getPageable(),
            vOrganizations.getTotalElements()
        );
    }

    @Override
    public List<VOrganization> fetchBagRelationships(List<VOrganization> vOrganizations) {
        return Optional.of(vOrganizations).map(this::fetchVUsers).map(this::fetchVRoles).orElse(Collections.emptyList());
    }

    VOrganization fetchVUsers(VOrganization result) {
        return entityManager
            .createQuery(
                "select vOrganization from VOrganization vOrganization left join fetch vOrganization.vUsers where vOrganization is :vOrganization",
                VOrganization.class
            )
            .setParameter("vOrganization", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<VOrganization> fetchVUsers(List<VOrganization> vOrganizations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, vOrganizations.size()).forEach(index -> order.put(vOrganizations.get(index).getId(), index));
        List<VOrganization> result = entityManager
            .createQuery(
                "select distinct vOrganization from VOrganization vOrganization left join fetch vOrganization.vUsers where vOrganization in :vOrganizations",
                VOrganization.class
            )
            .setParameter("vOrganizations", vOrganizations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    VOrganization fetchVRoles(VOrganization result) {
        return entityManager
            .createQuery(
                "select vOrganization from VOrganization vOrganization left join fetch vOrganization.vRoles where vOrganization is :vOrganization",
                VOrganization.class
            )
            .setParameter("vOrganization", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<VOrganization> fetchVRoles(List<VOrganization> vOrganizations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, vOrganizations.size()).forEach(index -> order.put(vOrganizations.get(index).getId(), index));
        List<VOrganization> result = entityManager
            .createQuery(
                "select distinct vOrganization from VOrganization vOrganization left join fetch vOrganization.vRoles where vOrganization in :vOrganizations",
                VOrganization.class
            )
            .setParameter("vOrganizations", vOrganizations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
