package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VPermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VPermission.class);
        VPermission vPermission1 = new VPermission();
        vPermission1.setId(1L);
        VPermission vPermission2 = new VPermission();
        vPermission2.setId(vPermission1.getId());
        assertThat(vPermission1).isEqualTo(vPermission2);
        vPermission2.setId(2L);
        assertThat(vPermission1).isNotEqualTo(vPermission2);
        vPermission1.setId(null);
        assertThat(vPermission1).isNotEqualTo(vPermission2);
    }
}
