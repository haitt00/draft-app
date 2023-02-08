package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VOrganizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VOrganizationDTO.class);
        VOrganizationDTO vOrganizationDTO1 = new VOrganizationDTO();
        vOrganizationDTO1.setId(1L);
        VOrganizationDTO vOrganizationDTO2 = new VOrganizationDTO();
        assertThat(vOrganizationDTO1).isNotEqualTo(vOrganizationDTO2);
        vOrganizationDTO2.setId(vOrganizationDTO1.getId());
        assertThat(vOrganizationDTO1).isEqualTo(vOrganizationDTO2);
        vOrganizationDTO2.setId(2L);
        assertThat(vOrganizationDTO1).isNotEqualTo(vOrganizationDTO2);
        vOrganizationDTO1.setId(null);
        assertThat(vOrganizationDTO1).isNotEqualTo(vOrganizationDTO2);
    }
}
