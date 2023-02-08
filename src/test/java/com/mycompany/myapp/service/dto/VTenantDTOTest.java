package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VTenantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VTenantDTO.class);
        VTenantDTO vTenantDTO1 = new VTenantDTO();
        vTenantDTO1.setId(1L);
        VTenantDTO vTenantDTO2 = new VTenantDTO();
        assertThat(vTenantDTO1).isNotEqualTo(vTenantDTO2);
        vTenantDTO2.setId(vTenantDTO1.getId());
        assertThat(vTenantDTO1).isEqualTo(vTenantDTO2);
        vTenantDTO2.setId(2L);
        assertThat(vTenantDTO1).isNotEqualTo(vTenantDTO2);
        vTenantDTO1.setId(null);
        assertThat(vTenantDTO1).isNotEqualTo(vTenantDTO2);
    }
}
