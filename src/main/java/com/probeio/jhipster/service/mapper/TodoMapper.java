package com.probeio.jhipster.service.mapper;


import com.probeio.jhipster.domain.*;
import com.probeio.jhipster.service.dto.TodoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Todo} and its DTO {@link TodoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TodoMapper extends EntityMapper<TodoDTO, Todo> {



    default Todo fromId(String id) {
        if (id == null) {
            return null;
        }
        Todo todo = new Todo();
        todo.setId(id);
        return todo;
    }
}
