package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VOrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VOrganization.class);
        VOrganization vOrganization1 = new VOrganization();
        vOrganization1.setId(1L);
        VOrganization vOrganization2 = new VOrganization();
        vOrganization2.setId(vOrganization1.getId());
        assertThat(vOrganization1).isEqualTo(vOrganization2);
        vOrganization2.setId(2L);
        assertThat(vOrganization1).isNotEqualTo(vOrganization2);
        vOrganization1.setId(null);
        assertThat(vOrganization1).isNotEqualTo(vOrganization2);
    }
}
