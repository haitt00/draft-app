package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VRole.class);
        VRole vRole1 = new VRole();
        vRole1.setId(1L);
        VRole vRole2 = new VRole();
        vRole2.setId(vRole1.getId());
        assertThat(vRole1).isEqualTo(vRole2);
        vRole2.setId(2L);
        assertThat(vRole1).isNotEqualTo(vRole2);
        vRole1.setId(null);
        assertThat(vRole1).isNotEqualTo(vRole2);
    }
}
