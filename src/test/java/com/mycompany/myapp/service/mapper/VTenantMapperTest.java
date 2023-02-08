package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VTenantMapperTest {

    private VTenantMapper vTenantMapper;

    @BeforeEach
    public void setUp() {
        vTenantMapper = new VTenantMapperImpl();
    }
}
