package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VAbstractAuditingEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VAbstractAuditingEntity.class);
        VAbstractAuditingEntity vAbstractAuditingEntity1 = new VAbstractAuditingEntity();
        vAbstractAuditingEntity1.setId(1L);
        VAbstractAuditingEntity vAbstractAuditingEntity2 = new VAbstractAuditingEntity();
        vAbstractAuditingEntity2.setId(vAbstractAuditingEntity1.getId());
        assertThat(vAbstractAuditingEntity1).isEqualTo(vAbstractAuditingEntity2);
        vAbstractAuditingEntity2.setId(2L);
        assertThat(vAbstractAuditingEntity1).isNotEqualTo(vAbstractAuditingEntity2);
        vAbstractAuditingEntity1.setId(null);
        assertThat(vAbstractAuditingEntity1).isNotEqualTo(vAbstractAuditingEntity2);
    }
}
