package com.probeio.jhipster.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.probeio.jhipster.web.rest.TestUtil;

public class TodoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Todo.class);
        Todo todo1 = new Todo();
        todo1.setId("id1");
        Todo todo2 = new Todo();
        todo2.setId(todo1.getId());
        assertThat(todo1).isEqualTo(todo2);
        todo2.setId("id2");
        assertThat(todo1).isNotEqualTo(todo2);
        todo1.setId(null);
        assertThat(todo1).isNotEqualTo(todo2);
    }
}
