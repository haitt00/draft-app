package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VUser.class);
        VUser vUser1 = new VUser();
        vUser1.setId(1L);
        VUser vUser2 = new VUser();
        vUser2.setId(vUser1.getId());
        assertThat(vUser1).isEqualTo(vUser2);
        vUser2.setId(2L);
        assertThat(vUser1).isNotEqualTo(vUser2);
        vUser1.setId(null);
        assertThat(vUser1).isNotEqualTo(vUser2);
    }
}
