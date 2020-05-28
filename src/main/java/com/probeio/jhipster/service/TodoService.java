package com.probeio.jhipster.service;

import com.probeio.jhipster.domain.Todo;
import com.probeio.jhipster.repository.TodoRepository;
import com.probeio.jhipster.service.dto.TodoDTO;
import com.probeio.jhipster.service.mapper.TodoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service Implementation for managing {@link Todo}.
 */
@Service
public class TodoService {

    private final Logger log = LoggerFactory.getLogger(TodoService.class);

    private final TodoRepository todoRepository;

    private final TodoMapper todoMapper;

    public TodoService(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    /**
     * Save a todo.
     *
     * @param todoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TodoDTO> save(TodoDTO todoDTO) {
        log.debug("Request to save Todo : {}", todoDTO);
        return todoRepository.save(todoMapper.toEntity(todoDTO))
            .map(todoMapper::toDto)
;    }

    /**
     * Get all the todos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<TodoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Todos");
        return todoRepository.findAllBy(pageable)
            .map(todoMapper::toDto);
    }


    /**
    * Returns the number of todos available.
    *
    */
    public Mono<Long> countAll() {
        return todoRepository.count();
    }

    /**
     * Get one todo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<TodoDTO> findOne(String id) {
        log.debug("Request to get Todo : {}", id);
        return todoRepository.findById(id)
            .map(todoMapper::toDto);
    }

    /**
     * Delete the todo by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Todo : {}", id);
        return todoRepository.deleteById(id);
    }
}
