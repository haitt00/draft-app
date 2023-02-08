package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VUserDTO.class);
        VUserDTO vUserDTO1 = new VUserDTO();
        vUserDTO1.setId(1L);
        VUserDTO vUserDTO2 = new VUserDTO();
        assertThat(vUserDTO1).isNotEqualTo(vUserDTO2);
        vUserDTO2.setId(vUserDTO1.getId());
        assertThat(vUserDTO1).isEqualTo(vUserDTO2);
        vUserDTO2.setId(2L);
        assertThat(vUserDTO1).isNotEqualTo(vUserDTO2);
        vUserDTO1.setId(null);
        assertThat(vUserDTO1).isNotEqualTo(vUserDTO2);
    }
}
