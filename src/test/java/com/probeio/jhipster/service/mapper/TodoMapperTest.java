package com.probeio.jhipster.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TodoMapperTest {

    private TodoMapper todoMapper;

    @BeforeEach
    public void setUp() {
        todoMapper = new TodoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(todoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(todoMapper.fromId(null)).isNull();
    }
}
