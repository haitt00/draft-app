package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VUserMapperTest {

    private VUserMapper vUserMapper;

    @BeforeEach
    public void setUp() {
        vUserMapper = new VUserMapperImpl();
    }
}
