package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VRoleDTO.class);
        VRoleDTO vRoleDTO1 = new VRoleDTO();
        vRoleDTO1.setId(1L);
        VRoleDTO vRoleDTO2 = new VRoleDTO();
        assertThat(vRoleDTO1).isNotEqualTo(vRoleDTO2);
        vRoleDTO2.setId(vRoleDTO1.getId());
        assertThat(vRoleDTO1).isEqualTo(vRoleDTO2);
        vRoleDTO2.setId(2L);
        assertThat(vRoleDTO1).isNotEqualTo(vRoleDTO2);
        vRoleDTO1.setId(null);
        assertThat(vRoleDTO1).isNotEqualTo(vRoleDTO2);
    }
}
