package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VTenantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VTenant.class);
        VTenant vTenant1 = new VTenant();
        vTenant1.setId(1L);
        VTenant vTenant2 = new VTenant();
        vTenant2.setId(vTenant1.getId());
        assertThat(vTenant1).isEqualTo(vTenant2);
        vTenant2.setId(2L);
        assertThat(vTenant1).isNotEqualTo(vTenant2);
        vTenant1.setId(null);
        assertThat(vTenant1).isNotEqualTo(vTenant2);
    }
}
